package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openapitools.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.Menu;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.RestaurantMenuAssociation;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.RestaurantMenuAssociationId;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.mapper.MenuMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.repository.MenuRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.repository.RestaurantMenuAssociationRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.repository.RestaurantRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMenuAssociationRepository restaurantMenuAssociationRepository;
    private final MenuMapper menuMapper;

    @Transactional
    public CreateMenuResponseDto createMenu(CreateMenuDto createMenuDto) {
        // TODO: only RESTAURANT_ADMIN can create menus. To be modified later - forbidden
        val restaurant = restaurantRepository.findById(createMenuDto.getIdRestaurant())
                .orElseThrow(() ->
                        new NotFoundException(String.format(
                                ErrorMessageUtils.RESTAURANT_NOT_FOUND,
                                createMenuDto.getIdRestaurant())
                        ));

        val menu = menuRepository.findByNameAndIngredientsList(
                createMenuDto.getName(),
                createMenuDto.getIngredientsList()
        ).orElseGet(() -> menuRepository.save(menuMapper.toMenu(createMenuDto)));

        restaurantMenuAssociationRepository.findByRestaurantIdAndMenuId(restaurant.getId(), menu.getId())
                .ifPresent(restaurantMenuAssociation -> {
                    throw new BadRequestException(
                            String.format(ErrorMessageUtils.RESTAURANT_MENU_ALREADY_EXISTS,
                                    restaurant.getId(),
                                    menu.getId())
                    );
                });

        RestaurantMenuAssociationId restaurantMenuAssociationId = new RestaurantMenuAssociationId(restaurant.getId(), menu.getId());
        RestaurantMenuAssociation association = new RestaurantMenuAssociation();
        association.setId(restaurantMenuAssociationId);

        restaurantMenuAssociationRepository.save(association);

        return menuMapper.toCreateMenuResponseDto(menu);
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        // TODO: only RESTAURANT_ADMIN can delete menus. To be modified later - forbidden
        val menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.MENU_NOT_FOUND, menuId)));

        List<RestaurantMenuAssociation> associations = restaurantMenuAssociationRepository.findByMenuId(menuId);
        restaurantMenuAssociationRepository.deleteAll(associations);

        menuRepository.delete(menu);
    }

    @Transactional
    public UpdateMenuResponseDto updateMenu(UpdateMenuDto updateMenuDto) {
        // TODO: only RESTAURANT_ADMIN can update menus. To be modified later - forbidden
        val menu = menuRepository.findById(updateMenuDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.MENU_NOT_FOUND, updateMenuDto.getId())));

        menu.setName(updateMenuDto.getName());
        menu.setIngredientsList(updateMenuDto.getIngredientsList());
        menu.setPrice(updateMenuDto.getPrice());
        menu.setDiscount(updateMenuDto.getDiscount());
        menu.setPhoto(updateMenuDto.getPhoto());
        menuRepository.save(menu);

        return menuMapper.toUpdateMenuResponseDto(menu);
    }

    @Transactional
    public MenuDetailsDto getMenuById(Long menuId) {
        // TODO: only RESTAURANT_ADMIN can get menus. To be modified later - forbidden
        val menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.MENU_NOT_FOUND, menuId)));

        return menuMapper.toMenuDetailsDto(menu);
    }

    @Transactional
    public List<MenuDetailsDto> getAllMenusByRestaurantId(Long restaurantId) {
        // TODO: only RESTAURANT_ADMIN can get menus. To be modified later - forbidden
        List<RestaurantMenuAssociation> associations = restaurantMenuAssociationRepository.findByRestaurantId(restaurantId);

        if (associations.isEmpty()) {
            throw new NotFoundException(String.format(ErrorMessageUtils.RESTAURANT_NOT_FOUND, restaurantId));
        }

        return associations.stream()
                .map(RestaurantMenuAssociation::getMenu)
                .distinct()
                .map(menuMapper::toMenuDetailsDto)
                .collect(Collectors.toList());
    }
}
