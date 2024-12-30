package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.distancecalculator.DistanceCalculator;
import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.OrderCreationDto;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.OrderCreationItemDto;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.OrderCreationResponseDto;
import ro.fmi.unibuc.licitatie_curieri.domain.menuitem.repository.MenuItemRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.OrderMenuItemAssociation;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.OrderMenuItemAssociationId;
import ro.fmi.unibuc.licitatie_curieri.domain.order.mapper.OrderMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.order.repository.OrderRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserInformationService userInformationService;

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

        val persistedOrder = orderRepository.save(order);
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

        return orderMapper.mapToOrderCreationResponse(persistedOrder);
    }

    private Restaurant getRestaurant(OrderCreationDto orderCreationDto) {
        val menuItem = menuItemRepository.findById(orderCreationDto.getItems().getFirst().getId())
                .orElseThrow(() -> new BadRequestException(ErrorMessageUtils.MENU_ITEM_NOT_FOUND));

        return menuItem.getRestaurant();
    }
}