package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openapitools.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.RestaurantMenuAssociation;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.RestaurantMenuAssociationId;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.mapper.MenuMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.repository.MenuRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.repository.RestaurantMenuAssociationRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.repository.RestaurantRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;
import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantMenuAssociationRepository restaurantMenuAssociationRepository;
    private final MenuMapper menuMapper;

    @Transactional
    public CreateMenuResponseDto createMenu(CreateMenuDto createMenuDto, String userId) {
        val user = userRepository.findById(Long.valueOf(userId)).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_ADMIN_REST_CAN_CREATE_MENUS);
        }

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
    public void deleteMenu(Long menuId, String userId) {
        val user = userRepository.findById(Long.valueOf(userId)).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_ADMIN_REST_CAN_DELETE_MENUS);
        }

        val menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.MENU_NOT_FOUND, menuId)));

        List<RestaurantMenuAssociation> associations = restaurantMenuAssociationRepository.findByMenuId(menuId);
        restaurantMenuAssociationRepository.deleteAll(associations);

        menuRepository.delete(menu);
    }

    @Transactional
    public UpdateMenuResponseDto updateMenu(UpdateMenuDto updateMenuDto, String userId) {
        val user = userRepository.findById(Long.valueOf(userId)).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_ADMIN_REST_CAN_UPDATE_MENUS);
        }

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

    @Transactional(readOnly = true)
    public MenuDetailsDto getMenuById(Long menuId, String userId) {
        val user = userRepository.findById(Long.valueOf(userId)).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT && user.getUserType() != UserType.CLIENT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_AND_ADMIN_REST_CAN_GET_MENUS);
        }

        val menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.MENU_NOT_FOUND, menuId)));

        return menuMapper.toMenuDetailsDto(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuDetailsDto> getAllMenusByRestaurantId(Long restaurantId, String userId) {
        val user = userRepository.findById(Long.valueOf(userId)).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT && user.getUserType() != UserType.CLIENT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_AND_ADMIN_REST_CAN_GET_MENUS);
        }

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
