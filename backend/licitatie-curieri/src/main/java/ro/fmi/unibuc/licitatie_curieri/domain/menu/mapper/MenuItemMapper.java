package ro.fmi.unibuc.licitatie_curieri.domain.menu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.MenuItemCreationDto;
import org.openapitools.model.MenuItemCreationResponseDto;
import org.openapitools.model.MenuItemDetailsDto;
import org.openapitools.model.MenuItemUpdateResponseDto;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.MenuItem;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MenuItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wasRemoved", constant = "false")
    @Mapping(target = "name", source = "menuItemCreationDto.name")
    MenuItem toMenuItem(MenuItemCreationDto menuItemCreationDto, Restaurant restaurant);

    MenuItemCreationResponseDto toMenuItemCreationResponseDto(MenuItem menuItem);

    MenuItemUpdateResponseDto toMenuItemUpdateResponseDto(MenuItem menuItem);

    MenuItemDetailsDto toMenuItemDetailsDto(MenuItem menuItem);
}
