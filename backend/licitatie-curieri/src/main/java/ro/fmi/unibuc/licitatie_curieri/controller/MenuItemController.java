package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.menuitem.api.MenuItemApi;
import ro.fmi.unibuc.licitatie_curieri.controller.menuitem.models.*;
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

        return menuItemService.getAllMenuItemsForRestaurant(restaurantId);
    }

    @Override
    public MenuItemDetailsDto getMenuItem(@PathVariable(value = "menu_item_id") Long menuItemId) {
        log.info(String.format(LogMessageUtils.GET_MENU_ITEM, menuItemId));

        return menuItemService.getMenuItem(menuItemId);
    }

    @Override
    public MenuItemCreationResponseDto createMenuItem(@RequestBody MenuItemCreationDto menuItemCreationDto) {
        log.info(String.format(LogMessageUtils.CREATE_MENU_ITEM, menuItemCreationDto.getRestaurantId()));

        return menuItemService.createMenuItem(menuItemCreationDto);
    }

    @Override
    public MenuItemUpdateResponseDto updateMenuItem(@PathVariable(value = "menu_item_id") Long menuItemId, @RequestBody MenuItemUpdateDto updateMenuDto) {
        log.info(String.format(LogMessageUtils.UPDATE_MENU_ITEM, menuItemId));

        return menuItemService.updateMenuItem(menuItemId, updateMenuDto);
    }

    @Override
    public void removeMenuItem(@PathVariable(value = "menu_item_id") Long menuItemId) {
        log.info(String.format(LogMessageUtils.REMOVE_MENU_ITEM, menuItemId));

        menuItemService.removeMenuItem(menuItemId);
    }
}
