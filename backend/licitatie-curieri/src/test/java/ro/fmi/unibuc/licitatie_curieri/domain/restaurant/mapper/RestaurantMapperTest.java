package ro.fmi.unibuc.licitatie_curieri.domain.restaurant.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.fmi.unibuc.licitatie_curieri.fixtures.RestaurantFixtures;

@SpringBootTest(classes = {RestaurantMapperImpl.class})
class RestaurantMapperTest {
    @Autowired
    private RestaurantMapper restaurantMapper;

    @Test
    void testMapToRestaurantDetailsDto() {
        val restaurant = RestaurantFixtures.getRestaurantFixture();

        val restaurantDetailsDto = restaurantMapper.toRestaurantDetailsDto(restaurant);

        Assertions.assertNotNull(restaurantDetailsDto);
        Assertions.assertEquals(restaurant.getId(), restaurantDetailsDto.getId());
        Assertions.assertEquals(restaurant.getName(), restaurantDetailsDto.getName());
        Assertions.assertEquals(restaurant.getAddress().getLatitude(), restaurantDetailsDto.getLatitude());
        Assertions.assertEquals(restaurant.getAddress().getLongitude(), restaurantDetailsDto.getLongitude());
    }
}
