package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.menuitem.models.*;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.mapper.MenuItemMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.repository.MenuItemRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.repository.RestaurantRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;
    private final UserInformationService userInformationService;

    @Transactional(readOnly = true)
    public List<MenuItemDetailsDto> getAllMenuItemsForRestaurant(Long restaurantId) {
        userInformationService.ensureCurrentUserIsVerified();

        if(!userInformationService.isCurrentUserClient() && !userInformationService.isCurrentUserRestaurantAdmin()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_AND_RESTAURANT_ADMIN_CAN_GET_MENU_ITEMS);
        }

        val restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.RESTAURANT_NOT_FOUND, restaurantId)));

        return restaurant.getMenuItems().stream()
                .filter(menuItem -> !menuItem.getWasRemoved())
                .map(menuItemMapper::toMenuItemDetailsDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public MenuItemDetailsDto getMenuItem(Long menuItemId) {
       userInformationService.ensureCurrentUserIsVerified();

        if(!userInformationService.isCurrentUserClient() && !userInformationService.isCurrentUserRestaurantAdmin()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_AND_RESTAURANT_ADMIN_CAN_GET_MENU_ITEM);
        }

        val menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.MENU_ITEM_NOT_FOUND, menuItemId)));

        return menuItemMapper.toMenuItemDetailsDto(menuItem);
    }


    @Transactional
    public MenuItemCreationResponseDto createMenuItem(MenuItemCreationDto menuItemCreationDto) {
        userInformationService.ensureCurrentUserIsVerified();

        if(!userInformationService.isCurrentUserRestaurantAdmin()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_RESTAURANT_ADMIN_CAN_CREATE_MENU_ITEMS);
        }

        val restaurant = restaurantRepository.findById(menuItemCreationDto.getRestaurantId())
                .orElseThrow(() ->
                        new NotFoundException(String.format(
                                ErrorMessageUtils.RESTAURANT_NOT_FOUND,
                                menuItemCreationDto.getRestaurantId())
                        ));

        val menuItem = menuItemMapper.toMenuItem(menuItemCreationDto, restaurant);

        val persistedMenuItem = menuItemRepository.save(menuItem);

        return menuItemMapper.toMenuItemCreationResponseDto(persistedMenuItem);
    }


    @Transactional
    public MenuItemUpdateResponseDto updateMenuItem(Long menuItemId, MenuItemUpdateDto menuItemUpdateDto) {
       userInformationService.ensureCurrentUserIsVerified();

        if(!userInformationService.isCurrentUserRestaurantAdmin()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_RESTAURANT_ADMIN_CAN_UPDATE_MENU_ITEMS);
        }

        val menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.MENU_ITEM_NOT_FOUND, menuItemId)));

        menuItem.setName(menuItemUpdateDto.getName());
        menuItem.setIngredientsList(menuItemUpdateDto.getIngredientsList());
        menuItem.setPrice(menuItemUpdateDto.getPrice());
        menuItem.setDiscount(menuItemUpdateDto.getDiscount());
        menuItem.setPhoto(menuItemUpdateDto.getPhoto());

        menuItemRepository.save(menuItem);

        return menuItemMapper.toMenuItemUpdateResponseDto(menuItem);
    }

    @Transactional
    public void removeMenuItem(Long menuItemId) {
        userInformationService.ensureCurrentUserIsVerified();

        if(!userInformationService.isCurrentUserRestaurantAdmin()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_RESTAURANT_ADMIN_CAN_REMOVE_MENU_ITEMS);
        }

        val menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.MENU_ITEM_NOT_FOUND, menuItemId)));

        menuItem.setWasRemoved(true);

        menuItemRepository.save(menuItem);
    }
}
