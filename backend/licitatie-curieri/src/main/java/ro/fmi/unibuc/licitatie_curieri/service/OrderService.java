package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.OrderCreationDto;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.OrderCreationItemDto;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.OrderCreationResponseDto;
import ro.fmi.unibuc.licitatie_curieri.domain.menuitem.repository.MenuItemRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MenuItemRepository menuItemRepository;
    private final UserInformationService userInformationService;

    @Transactional
    public OrderCreationResponseDto createOrder(OrderCreationDto orderCreationDto) {
        userInformationService.ensureCurrentUserIsVerified();
        if (!userInformationService.isCurrentUserClient()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_CAN_CREATE_ORDERS);
        }

        val restaurant = getRestaurant(orderCreationDto);

        val menuItemIds = orderCreationDto.getItems().stream()
                .map(OrderCreationItemDto::getId)
                .collect(Collectors.toSet());
        val menuItems = menuItemRepository.findAllByIdsAndRestaurantId(menuItemIds, restaurant.getId());

        if (menuItems.size() != menuItemIds.size()) {
            throw new BadRequestException(ErrorMessageUtils.NOT_ALL_MENU_ITEMS_BELONG_TO_SAME_RESTAURANT);
        }

        return null;
    }

    private Restaurant getRestaurant(OrderCreationDto orderCreationDto) {
        val menuItem = menuItemRepository.findById(orderCreationDto.getItems().getFirst().getId())
                .orElseThrow(() -> new BadRequestException(ErrorMessageUtils.MENU_ITEM_NOT_FOUND));

        return menuItem.getRestaurant();
    }
}