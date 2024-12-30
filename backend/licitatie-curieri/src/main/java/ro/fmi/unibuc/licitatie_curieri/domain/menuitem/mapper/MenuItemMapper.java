package ro.fmi.unibuc.licitatie_curieri.domain.menuitem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ro.fmi.unibuc.licitatie_curieri.controller.menuitem.models.*;
import ro.fmi.unibuc.licitatie_curieri.domain.menuitem.entity.MenuItem;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MenuItemMapper {
    MenuItemDetailsDto toMenuItemDetailsDto(MenuItem menuItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wasRemoved", constant = "false")
    @Mapping(target = "name", source = "menuItemCreationDto.name")
    MenuItem toMenuItem(MenuItemCreationDto menuItemCreationDto, Restaurant restaurant);

    MenuItemCreationResponseDto toMenuItemCreationResponseDto(MenuItem menuItem);

    MenuItemUpdateResponseDto toMenuItemUpdateResponseDto(MenuItem menuItem);
}
