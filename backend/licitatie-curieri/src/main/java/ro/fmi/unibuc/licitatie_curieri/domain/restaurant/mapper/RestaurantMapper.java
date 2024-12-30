package ro.fmi.unibuc.licitatie_curieri.domain.restaurant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ro.fmi.unibuc.licitatie_curieri.controller.restaurant.models.RestaurantCreationDto;
import ro.fmi.unibuc.licitatie_curieri.controller.restaurant.models.RestaurantCreationResponseDto;
import ro.fmi.unibuc.licitatie_curieri.controller.restaurant.models.RestaurantDetailsDto;
import ro.fmi.unibuc.licitatie_curieri.controller.restaurant.models.RestaurantUpdateResponseDto;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RestaurantMapper {
    RestaurantDetailsDto toRestaurantDetailsDto(Restaurant restaurant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "details", source = "addressDetails")
    Address toAddress(RestaurantCreationDto restaurantCreationDto);

    @Mapping(target = "addressId", source = "address.id")
    @Mapping(target = "addressDetails", source = "address.details")
    @Mapping(target = "latitude", source = "address.latitude")
    @Mapping(target = "longitude", source = "address.longitude")
    RestaurantCreationResponseDto toRestaurantCreationResponseDto(Restaurant restaurant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "menuItems", ignore = true)
    @Mapping(target = "name", source = "createRestaurantDto.name")
    @Mapping(target = "wasRemoved", constant = "false")
    Restaurant toRestaurant(RestaurantCreationDto createRestaurantDto, Address address);

    @Mapping(target = "addressId", source = "address.id")
    @Mapping(target = "addressDetails", source = "address.details")
    @Mapping(target = "latitude", source = "address.latitude")
    @Mapping(target = "longitude", source = "address.longitude")
    RestaurantUpdateResponseDto toRestaurantUpdateResponseDto(Restaurant restaurant);
}
