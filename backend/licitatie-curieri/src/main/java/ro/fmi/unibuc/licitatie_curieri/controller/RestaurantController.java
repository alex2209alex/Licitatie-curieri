package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.restaurant.api.RestaurantApi;
import ro.fmi.unibuc.licitatie_curieri.controller.restaurant.models.*;
import ro.fmi.unibuc.licitatie_curieri.service.RestaurantService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RestaurantController implements RestaurantApi {
    private final RestaurantService restaurantService;

    @Override
    public List<RestaurantDetailsDto> getRestaurants(@RequestParam(value = "address_id", required = false) Long addressId) {
        log.info(LogMessageUtils.GET_RESTAURANTS);

        return restaurantService.getRestaurants(addressId);
    }

    @Override
    public RestaurantDetailsDto getRestaurant(@PathVariable(value = "restaurant_id") Long restaurantId) {
        log.info(String.format(LogMessageUtils.GET_RESTAURANT, restaurantId));

        return restaurantService.getRestaurant(restaurantId);
    }

    @Override
    public RestaurantCreationResponseDto createRestaurant(@RequestBody RestaurantCreationDto createRestaurantDto) {
        log.info(String.format(LogMessageUtils.CREATE_RESTAURANT, createRestaurantDto.getName()));

        return restaurantService.createRestaurant(createRestaurantDto);
    }

    @Override
    public RestaurantUpdateResponseDto updateRestaurant(@PathVariable("restaurant_id") Long restaurantId, @RequestBody RestaurantUpdateDto restaurantUpdateDto) {
        log.info(String.format(LogMessageUtils.UPDATE_RESTAURANT, restaurantId));

        return restaurantService.updateRestaurant(restaurantId, restaurantUpdateDto);
    }

    @Override
    public void removeRestaurant(@PathVariable(value = "restaurant_id") Long restaurantId) {
        log.info(String.format(LogMessageUtils.REMOVE_RESTAURANT, restaurantId));

        restaurantService.removeRestaurant(restaurantId);
    }
}
