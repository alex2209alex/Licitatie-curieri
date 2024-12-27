package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.MenuApi;
import org.openapitools.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.service.MenuService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MenuController implements MenuApi {
    private final MenuService menuService;

    @Override
    public CreateMenuResponseDto createMenu(@RequestBody CreateMenuDto createMenuDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null) ? authentication.getName() : null;

        log.info(String.format(LogMessageUtils.CREATE_MENU,
                createMenuDto.getIdRestaurant(),
                createMenuDto.getName(),
                createMenuDto.getPrice(),
                createMenuDto.getIngredientsList(),
                createMenuDto.getPhoto(),
                createMenuDto.getDiscount()
        ));
        return menuService.createMenu(createMenuDto, email);
    }

    @Override
    public void deleteMenu(@RequestParam(value = "menu_id") Long menuId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null) ? authentication.getName() : null;

        log.info(String.format(LogMessageUtils.DELETE_MENU, menuId));
        menuService.deleteMenu(menuId, email);
    }

    @Override
    public UpdateMenuResponseDto updateMenu(@RequestBody UpdateMenuDto updateMenuDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null) ? authentication.getName() : null;

        log.info(String.format(LogMessageUtils.UPDATE_MENU,
                updateMenuDto.getName(),
                updateMenuDto.getPrice(),
                updateMenuDto.getIngredientsList(),
                updateMenuDto.getPhoto(),
                updateMenuDto.getDiscount()
        ));
        return menuService.updateMenu(updateMenuDto, email);
    }

    @Override
    public MenuDetailsDto getMenuById(@RequestParam(value = "menu_id") Long menuId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null) ? authentication.getName() : null;

        log.info(String.format(LogMessageUtils.GET_MENU_BY_ID, menuId));
        return menuService.getMenuById(menuId, email);
    }

    @Override
    public List<MenuDetailsDto> getAllMenusByRestaurantId(@RequestParam(value = "restaurant_id") Long restaurantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null) ? authentication.getName() : null;

        log.info(String.format(LogMessageUtils.GET_ALL_MENUS_BY_RESTAURANT_ID, restaurantId));
        return menuService.getAllMenusByRestaurantId(restaurantId, email);
    }
}
