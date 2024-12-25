package ro.fmi.unibuc.licitatie_curieri.domain.menu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.CreateMenuDto;
import org.openapitools.model.CreateMenuResponseDto;
import org.openapitools.model.UpdateMenuResponseDto;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.Menu;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MenuMapper {
    @Mapping(target = "id", ignore = true)
    Menu toMenu(CreateMenuDto createMenuDto);

    CreateMenuResponseDto toCreateMenuResponseDto(Menu menu);

    UpdateMenuResponseDto toUpdateMenuResponseDto(Menu menu);
}
