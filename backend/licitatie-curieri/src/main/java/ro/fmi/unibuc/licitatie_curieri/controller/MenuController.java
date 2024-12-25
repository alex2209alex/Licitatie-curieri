package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.MenuApi;
import org.openapitools.model.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.service.MenuService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MenuController implements MenuApi {
    private final MenuService menuService;

    @Override
    public CreateMenuResponseDto createMenu(@RequestBody CreateMenuDto createMenuDto) {
        log.info(String.format(LogMessageUtils.CREATE_MENU,
                createMenuDto.getName(),
                createMenuDto.getPrice(),
                createMenuDto.getIngredientsList(),
                createMenuDto.getPhoto(),
                createMenuDto.getDiscount()
        ));
        return menuService.createMenu(createMenuDto);
    }

    @Override
    public void deleteMenu(@RequestParam(value = "menu_id") Long menuId) {
        log.info(String.format(LogMessageUtils.DELETE_MENU, menuId));

        menuService.deleteMenu(menuId);
    }

    @Override
    public UpdateMenuResponseDto updateMenu(@RequestBody UpdateMenuDto updateMenuDto) {
        log.info(String.format(LogMessageUtils.UPDATE_MENU,
                updateMenuDto.getName(),
                updateMenuDto.getPrice(),
                updateMenuDto.getIngredientsList(),
                updateMenuDto.getPhoto(),
                updateMenuDto.getDiscount()
        ));

        return menuService.updateMenu(updateMenuDto);
    }

    @Override
    public MenuDetailsDto getMenuById(@RequestParam(value = "menu_id") Long menuId) {
        log.info(String.format(LogMessageUtils.GET_MENU_BY_ID, menuId));
        return menuService.getMenuById(menuId);
    }
}
