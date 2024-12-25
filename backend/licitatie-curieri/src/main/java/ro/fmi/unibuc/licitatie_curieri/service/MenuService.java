package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openapitools.model.CreateMenuDto;
import org.openapitools.model.CreateMenuResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.mapper.MenuMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.repository.MenuRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    @Transactional
    public CreateMenuResponseDto createMenu(CreateMenuDto createMenuDto) {
        // TODO: only RESTAURANT_ADMIN can create menus. To be modified later
        menuRepository.findByNameAndIngredientsList(
                        createMenuDto.getName(),
                        createMenuDto.getIngredientsList()
                )
                .ifPresent(menu -> {
                    throw new BadRequestException(
                            String.format(ErrorMessageUtils.MENU_ALREADY_EXISTS,
                                    menu.getName())
                    );
                });

        val menu = menuRepository.save(menuMapper.toMenu(createMenuDto));
        return menuMapper.toCreateMenuResponseDto(menu);
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        //TODO: only MENU_ADMIN can delete menus
        val menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.MENU_NOT_FOUND, menuId)));

        menuRepository.delete(menu);
    }
}