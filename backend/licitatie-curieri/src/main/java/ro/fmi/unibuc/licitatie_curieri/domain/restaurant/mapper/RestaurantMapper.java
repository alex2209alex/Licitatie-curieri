package ro.fmi.unibuc.licitatie_curieri.domain.restaurant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.*;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RestaurantMapper {
    @Mapping(target = "latitude", source = "address.latitude")
    @Mapping(target = "longitude", source = "address.longitude")
    RestaurantDetailsDto toRestaurantDetailsDto(Restaurant restaurant);

    @Mapping(target = "details", source = "address")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    AddressCreationDto toAddressCreationDto(CreateRestaurantDto createRestaurantDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "createRestaurantDto.name")
    @Mapping(target = "address", source = "addressId", qualifiedByName = "mapAddressId")
    Restaurant toRestaurant(CreateRestaurantDto createRestaurantDto, Long addressId);

    CreateRestaurantResponseDto toCreateRestaurantResponseDto(Restaurant restaurant);

    UpdateRestaurantNameResponseDto toUpdateRestaurantNameResponseDto(Restaurant restaurant);

    @Named("mapAddressId")
    default Address mapAddressId(Long addressId) {
        if (addressId == null) {
            return null;
        }

        Address address = new Address();
        address.setId(addressId);
        return address;
    }
}
