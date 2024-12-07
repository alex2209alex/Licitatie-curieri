package ro.fmi.unibuc.licitatie_curieri.domain.restaurant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.RestaurantDetailsDto;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RestaurantMapper {
    @Mapping(target = "latitude", source = "address.latitude")
    @Mapping(target = "longitude", source = "address.longitude")
    RestaurantDetailsDto toRestaurantDetailsDto(Restaurant restaurant);
}
