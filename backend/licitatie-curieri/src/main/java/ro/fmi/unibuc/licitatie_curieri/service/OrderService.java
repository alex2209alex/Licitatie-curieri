package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.distancecalculator.DistanceCalculator;
import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.orderhandler.OrderHandler;
import ro.fmi.unibuc.licitatie_curieri.common.orderhandler.OrderThread;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.*;
import ro.fmi.unibuc.licitatie_curieri.controller.realtime.models.OfferDto;
import ro.fmi.unibuc.licitatie_curieri.controller.realtime.models.OfferResponseDto;
import ro.fmi.unibuc.licitatie_curieri.controller.realtime.models.OfferStatusDto;
import ro.fmi.unibuc.licitatie_curieri.domain.menuitem.repository.MenuItemRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.Order;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.OrderMenuItemAssociation;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.OrderMenuItemAssociationId;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.OrderStatus;
import ro.fmi.unibuc.licitatie_curieri.domain.order.mapper.OrderMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.order.repository.OrderRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociation;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociationId;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserInformationService userInformationService;
    private final OrderHandler orderHandler;

    @Transactional(readOnly = true)
    public List<OrderDetailsDto> getClientOrders() {
        userInformationService.ensureCurrentUserIsVerified();
        if (!userInformationService.isCurrentUserClient()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_CAN_GET_CLIENT_ORDERS);
        }

        val addressIds = userInformationService.getCurrentUser()
                .getUserAddressAssociations()
                .stream()
                .map(UserAddressAssociation::getId)
                .map(UserAddressAssociationId::getAddressId)
                .collect(Collectors.toSet());

        synchronized (OrderHandler.mutex) {
            return orderRepository.findAllByAddressIds(addressIds).stream()
                    .map(orderMapper::mapToOrderDetailsDto)
                    .sorted(Comparator.comparing(OrderDetailsDto::getAuctionDeadline))
                    .toList()
                    .reversed();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderDetailsDto> getNearbyOrders(Double latitude, Double longitude) {
        userInformationService.ensureCurrentUserIsVerified();
        if (!userInformationService.isCurrentUserCourier()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_COURIER_CAN_GET_NEARBY_ORDERS);
        }

        synchronized (OrderHandler.mutex) {
            return orderRepository.findAll().stream()
                    .filter(order -> OrderStatus.IN_AUCTION == order.getOrderStatus())
                    .filter(order -> DistanceCalculator.isWithinRange(
                                    order.getOrderMenuItemAssociations().getFirst().getMenuItem().getRestaurant().getAddress().getLatitude(),
                                    order.getOrderMenuItemAssociations().getFirst().getMenuItem().getRestaurant().getAddress().getLongitude(),
                                    latitude,
                                    longitude
                            )
                    )
                    .map(orderMapper::mapToOrderDetailsDto)
                    .sorted(Comparator.comparing(OrderDetailsDto::getAuctionDeadline))
                    .toList()
                    .reversed();
        }
    }

    @Transactional
    public OrderToDeliverDetailsDto getOrderDetails(Long orderId) {
        userInformationService.ensureCurrentUserIsVerified();
        if(!userInformationService.isCurrentUserCourier()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_COURIER_CAN_VIEW_ORDER_DETAILS);
        }

        Order order;
        synchronized (OrderHandler.mutex) {
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new BadRequestException(String.format(ErrorMessageUtils.ORDER_NOT_FOUND, orderId)));

            val currentUser = userInformationService.getCurrentUser();
            if(!order.getCourier().getId().equals(currentUser.getId())){
                throw new ForbiddenException(ErrorMessageUtils.COURIER_NOT_ASSOCIATED_WITH_ORDER);
            }
        }

        return orderMapper.mapToOrderToDeliverDetailsDto(order);
    }

    @Transactional
    public OrderCreationResponseDto createOrder(OrderCreationDto orderCreationDto) {
        userInformationService.ensureCurrentUserIsVerified();
        if (!userInformationService.isCurrentUserClient()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_CAN_CREATE_ORDERS);
        }

        val clientAddress = userInformationService.getCurrentUser().getUserAddressAssociations().stream()
                .map(UserAddressAssociation::getAddress)
                .filter(address -> address.getId().equals(orderCreationDto.getAddressId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.USER_ADDRESS_WITH_ID_NOT_FOUND, orderCreationDto.getAddressId())));

        val restaurant = getRestaurant(orderCreationDto);

        if (!DistanceCalculator.isWithinRange(clientAddress.getLatitude(), clientAddress.getLongitude(), restaurant.getAddress().getLatitude(), restaurant.getAddress().getLongitude())) {
            throw new BadRequestException(ErrorMessageUtils.ORDER_TOO_FAR);
        }

        val menuItemIds = orderCreationDto.getItems().stream()
                .map(OrderCreationItemDto::getId)
                .collect(Collectors.toSet());
        val menuItems = menuItemRepository.findAllByIdsAndRestaurantId(menuItemIds, restaurant.getId());

        if (menuItems.size() != menuItemIds.size()) {
            throw new BadRequestException(ErrorMessageUtils.NOT_ALL_MENU_ITEMS_BELONG_TO_SAME_RESTAURANT);
        }

        val menuItemPriceMap = new HashMap<Long, Double>();
        for (val menuItem : menuItems) {
            menuItemPriceMap.put(menuItem.getId(), menuItem.getPrice());
        }

        val menuItemQuantityHashMap = new HashMap<Long, Integer>();
        double foodPrice = 0;
        for (val orderCreationItemDto : orderCreationDto.getItems()) {
            if (menuItemQuantityHashMap.containsKey(orderCreationItemDto.getId())) {
                menuItemQuantityHashMap.put(orderCreationItemDto.getId(), menuItemQuantityHashMap.get(orderCreationItemDto.getId()) + orderCreationItemDto.getQuantity());
            } else {
                menuItemQuantityHashMap.put(orderCreationItemDto.getId(), orderCreationItemDto.getQuantity());
            }
            foodPrice += menuItemPriceMap.get(orderCreationItemDto.getId()) * orderCreationItemDto.getQuantity();
        }

        val order = orderMapper.mapToOrder(orderCreationDto, foodPrice, clientAddress);

        Order persistedOrder;
        synchronized (OrderHandler.mutex) {
            persistedOrder = orderRepository.save(order);
            persistedOrder.setOrderMenuItemAssociations(new ArrayList<>());
            menuItemQuantityHashMap.forEach((menuItemId, quantity) -> {
                val orderMenuItemAssociationId = new OrderMenuItemAssociationId();
                orderMenuItemAssociationId.setOrderId(persistedOrder.getId());
                orderMenuItemAssociationId.setMenuItemId(menuItemId);
                val orderMenuItemAssociation = new OrderMenuItemAssociation();
                orderMenuItemAssociation.setId(orderMenuItemAssociationId);
                orderMenuItemAssociation.setQuantity(quantity);
                persistedOrder.getOrderMenuItemAssociations().add(orderMenuItemAssociation);
            });
        }

        val orderThread = new OrderThread(orderHandler, persistedOrder.getId(), ChronoUnit.SECONDS.between(Instant.now(), persistedOrder.getAuctionDeadline()));
        orderThread.start();

        return orderMapper.mapToOrderCreationResponse(persistedOrder);
    }

    @Transactional
    public OfferResponseDto makeOffer(OfferDto offerDto) {
        userInformationService.ensureCurrentUserIsVerified();
        if (!userInformationService.isCurrentUserCourier()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_COURIER_CAN_MAKE_OFFERS);
        }

        synchronized (OrderHandler.mutex) {
            val order = orderRepository.findById(offerDto.getOrderId())
                    .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.ORDER_NOT_FOUND, offerDto.getOrderId())));

            if (OrderStatus.IN_AUCTION != order.getOrderStatus()) {
                return new OfferResponseDto(OfferStatusDto.REJECTED_AUCTION_IS_OVER, offerDto.getOrderId(), offerDto.getDeliveryPrice());
            }

            if (order.getDeliveryPriceLimit() < offerDto.getDeliveryPrice()) {
                return new OfferResponseDto(OfferStatusDto.REJECTED_OFFER_EXCEEDS_DELIVERY_PRICE_LIMIT, offerDto.getOrderId(), offerDto.getDeliveryPrice());
            }

            if (order.getDeliveryPrice() == null || order.getDeliveryPrice() > offerDto.getDeliveryPrice()) {
                order.setDeliveryPrice(offerDto.getDeliveryPrice());
                order.setCourier(userInformationService.getCurrentUser());
                return new OfferResponseDto(OfferStatusDto.ACCEPTED, offerDto.getOrderId(), offerDto.getDeliveryPrice());
            }

            return new OfferResponseDto(OfferStatusDto.REJECTED_NOT_LOWEST_OFFER, offerDto.getOrderId(), offerDto.getDeliveryPrice());
        }
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        userInformationService.ensureCurrentUserIsVerified();
        if (!userInformationService.isCurrentUserClient()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_CAN_CANCEL_ORDERS);
        }

        synchronized (OrderHandler.mutex) {
            val order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.ORDER_NOT_FOUND, orderId)));

            ensureOrderBelongsToClient(order);
            ensureOrderIsInAuction(order);

            order.setOrderStatus(OrderStatus.CANCELLED);
        }
    }

    private Restaurant getRestaurant(OrderCreationDto orderCreationDto) {
        val menuItem = menuItemRepository.findById(orderCreationDto.getItems().getFirst().getId())
                .orElseThrow(() -> new BadRequestException(ErrorMessageUtils.MENU_ITEM_NOT_FOUND));

        return menuItem.getRestaurant();
    }

    private void ensureOrderBelongsToClient(Order order) {
        val addressIds = userInformationService.getCurrentUser().getUserAddressAssociations().stream()
                .map(UserAddressAssociation::getId)
                .map(UserAddressAssociationId::getAddressId)
                .collect(Collectors.toSet());
        if (!addressIds.contains(order.getAddress().getId())) {
            throw new ForbiddenException(ErrorMessageUtils.ORDER_DOES_NOT_BELONG_TO_CLIENT);
        }
    }

    private void ensureOrderIsInAuction(Order order) {
        if (OrderStatus.IN_AUCTION != order.getOrderStatus()) {
            throw new ForbiddenException(ErrorMessageUtils.ORDER_CANNOT_BE_CANCELED);
        }
    }
}