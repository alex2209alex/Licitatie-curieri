package ro.fmi.unibuc.licitatie_curieri.fixtures;

import lombok.val;
import org.openapitools.model.RestaurantDetailsDto;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        restaurantDetailsDto.setLatitude(restaurant.getAddress().getLatitude());
        restaurantDetailsDto.setLongitude(restaurant.getAddress().getLongitude());
        return restaurantDetailsDto;
    }

    public static RestaurantDetailsDto getRestaurantDetailsDto() {
        val restaurantDetailsDto = new RestaurantDetailsDto();
        restaurantDetailsDto.setId(1L);
        restaurantDetailsDto.setName("name");
        restaurantDetailsDto.setLatitude(12.345);
        restaurantDetailsDto.setLongitude(54.321);
        return restaurantDetailsDto;
    }
}
