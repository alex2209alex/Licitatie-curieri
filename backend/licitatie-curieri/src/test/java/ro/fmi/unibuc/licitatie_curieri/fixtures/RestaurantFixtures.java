package ro.fmi.unibuc.licitatie_curieri.fixtures;

import lombok.val;
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
}
