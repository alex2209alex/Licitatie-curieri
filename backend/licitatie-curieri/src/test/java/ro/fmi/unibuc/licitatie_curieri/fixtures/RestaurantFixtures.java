package ro.fmi.unibuc.licitatie_curieri.fixtures;

import lombok.val;
import org.openapitools.model.RestaurantDetailsDto;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;

public class RestaurantFixtures {
    private RestaurantFixtures() {
    }

    public static Restaurant getRestaurantFixture() {
        val restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("name");
        restaurant.setAddress(AddressFixtures.getAddressFixture());
        return restaurant;
    }

    public static RestaurantDetailsDto getRestaurantDeatilsDto(Restaurant restaurant) {
        val restaurantDetailsDto = new RestaurantDetailsDto();
        restaurantDetailsDto.setId(restaurant.getId());
        restaurantDetailsDto.setName(restaurant.getName());
        restaurantDetailsDto.setLatitude(restaurant.getAddress().getLatitude());
        restaurantDetailsDto.setLongitude(restaurant.getAddress().getLongitude());
        return restaurantDetailsDto;
    }
}
