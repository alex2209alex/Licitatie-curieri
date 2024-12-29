package ro.fmi.unibuc.licitatie_curieri.fixtures;

import lombok.val;
import org.openapitools.model.AddressDetailsDto;
import org.openapitools.model.RestaurantDetailsDto;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;

public class RestaurantFixtures {
    private RestaurantFixtures() {
    }

    public static MultiValueMap<String, String> getEmptyRequestParametersFixture() {
        return new LinkedMultiValueMap<>();
    }

    public static Restaurant getRestaurantFixture() {
        val restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("name");
        restaurant.setAddress(AddressFixtures.getAddressFixture());
        return restaurant;
    }

    public static RestaurantDetailsDto getRestaurantDetailsDto(Restaurant restaurant) {
        val restaurantDetailsDto = new RestaurantDetailsDto();
        restaurantDetailsDto.setId(restaurant.getId());
        restaurantDetailsDto.setName(restaurant.getName());
        restaurantDetailsDto.setAddress(getAddressDetailsDto(restaurant.getAddress()));
        return restaurantDetailsDto;
    }

    public static RestaurantDetailsDto getRestaurantDetailsDto() {
        val restaurantDetailsDto = new RestaurantDetailsDto();
        restaurantDetailsDto.setId(1L);
        restaurantDetailsDto.setName("name");
        restaurantDetailsDto.setAddress(getAddressDetailsDto());
        return restaurantDetailsDto;
    }

    private static AddressDetailsDto getAddressDetailsDto() {
        val addressDetailsDto = new AddressDetailsDto();
        addressDetailsDto.setId(1L);
        addressDetailsDto.setDetails("details");
        addressDetailsDto.setLatitude(12.23);
        addressDetailsDto.setLongitude(43.51);
        return addressDetailsDto;
    }

    private static AddressDetailsDto getAddressDetailsDto(Address address) {
        val addressDetailsDto = new AddressDetailsDto();
        addressDetailsDto.setId(address.getId());
        addressDetailsDto.setDetails(address.getDetails());
        addressDetailsDto.setLatitude(address.getLatitude());
        addressDetailsDto.setLongitude(address.getLongitude());
        return addressDetailsDto;
    }
}
