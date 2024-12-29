package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.MenuItemApi;
import org.openapitools.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.service.MenuItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MenuItemController implements MenuItemApi {
    private final MenuItemService menuItemService;

    @Override
    public List<MenuItemDetailsDto> getAllMenuItemsForRestaurant(@RequestParam(value = "restaurantId") Long restaurantId) {
        log.info(String.format(LogMessageUtils.GET_ALL_MENU_ITEMS_FOR_RESTAURANT, restaurantId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null) ? authentication.getName() : null;

        return menuItemService.getAllMenuItemsForRestaurant(restaurantId, email);
    }

    @Override
    public MenuItemDetailsDto getMenuItem(@PathVariable(value = "menu_item_id") Long menuItemId) {
        log.info(String.format(LogMessageUtils.GET_MENU_ITEM, menuItemId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null) ? authentication.getName() : null;

        return menuItemService.getMenuItem(menuItemId, email);
    }

    @Override
    public MenuItemCreationResponseDto createMenuItem(@RequestBody MenuItemCreationDto menuItemCreationDto) {
        log.info(String.format(LogMessageUtils.CREATE_MENU_ITEM, menuItemCreationDto.getRestaurantId()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null) ? authentication.getName() : null;

        return menuItemService.createMenuItem(menuItemCreationDto, email);
    }

    @Override
    public MenuItemUpdateResponseDto updateMenuItem(@PathVariable(value = "menu_item_id") Long menuItemId, @RequestBody MenuItemUpdateDto updateMenuDto) {
        log.info(String.format(LogMessageUtils.UPDATE_MENU_ITEM, menuItemId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null) ? authentication.getName() : null;

        return menuItemService.updateMenuItem(menuItemId, updateMenuDto, email);
    }

    @Override
    public void removeMenuItem(@PathVariable(value = "menu_item_id") Long menuItemId) {
        log.info(String.format(LogMessageUtils.REMOVE_MENU_ITEM, menuItemId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null) ? authentication.getName() : null;

        menuItemService.deleteMenu(menuItemId, email);
    }
}
