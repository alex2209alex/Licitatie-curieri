package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openapitools.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.mapper.MenuItemMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.repository.MenuItemRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.repository.RestaurantRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;
import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuItemMapper menuItemMapper;

    @Transactional(readOnly = true)
    public List<MenuItemDetailsDto> getAllMenuItemsForRestaurant(Long restaurantId, String email) {
        val user = userRepository.findByEmail(email).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT && user.getUserType() != UserType.CLIENT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_AND_RESTAURANT_ADMIN_CAN_GET_MENU_ITEMS);
        }

        val restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.RESTAURANT_NOT_FOUND, restaurantId)));

        return restaurant.getMenuItems().stream()
                .filter(menuItem -> !menuItem.getWasRemoved())
                .map(menuItemMapper::toMenuItemDetailsDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public MenuItemDetailsDto getMenuItem(Long menuItemId, String email) {
        val user = userRepository.findByEmail(email).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT && user.getUserType() != UserType.CLIENT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_AND_RESTAURANT_ADMIN_CAN_GET_MENU_ITEMS);
        }

        val menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.MENU_ITEM_NOT_FOUND, menuItemId)));

        return menuItemMapper.toMenuItemDetailsDto(menuItem);
    }


    @Transactional
    public MenuItemCreationResponseDto createMenuItem(MenuItemCreationDto menuItemCreationDto, String email) {
        val user = userRepository.findByEmail(email).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT) {
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
    public MenuItemUpdateResponseDto updateMenuItem(Long menuItemId, MenuItemUpdateDto menuItemUpdateDto, String email) {
        val user = userRepository.findByEmail(email).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT) {
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
    public void deleteMenu(Long menuItemId, String email) {
        val user = userRepository.findByEmail(email).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_RESTAURANT_ADMIN_CAN_DELETE_MENU_ITEMS);
        }

        val menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.MENU_ITEM_NOT_FOUND, menuItemId)));

        menuItem.setWasRemoved(true);

        menuItemRepository.save(menuItem);
    }
}
