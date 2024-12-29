package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.RestaurantApi;
import org.openapitools.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.service.RestaurantService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RestaurantController implements RestaurantApi {
    private final RestaurantService restaurantService;

    @Override
    public List<RestaurantDetailsDto> getRestaurants(@RequestParam(value = "address_id") Long addressId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (authentication != null) ? authentication.getName() : null;

        log.info(String.format(LogMessageUtils.GET_RESTAURANTS, addressId));
        return restaurantService.getRestaurants(addressId, userId);
    }

    @Override
    public CreateRestaurantResponseDto createRestaurant(@RequestBody CreateRestaurantDto createRestaurantDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (authentication != null) ? authentication.getName() : null;

        log.info(String.format(LogMessageUtils.CREATE_RESTAURANT,
                createRestaurantDto.getName(),
                createRestaurantDto.getAddress(),
                createRestaurantDto.getLatitude(),
                createRestaurantDto.getLongitude()
        ));
        return restaurantService.createRestaurant(createRestaurantDto, userId);
    }

    @Override
    public void deleteRestaurant(@RequestParam(value = "restaurant_id") Long restaurantId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (authentication != null) ? authentication.getName() : null;

        log.info(String.format(LogMessageUtils.DELETE_RESTAURANT, restaurantId));
        restaurantService.deleteRestaurant(restaurantId, userId);
    }

    @Override
    public UpdateRestaurantNameResponseDto updateRestaurantByName(@RequestBody UpdateRestaurantNameDto updateRestaurantNameDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (authentication != null) ? authentication.getName() : null;

        log.info(String.format(LogMessageUtils.UPDATE_RESTAURANT_BY_NAME,
                updateRestaurantNameDto.getId(),
                updateRestaurantNameDto.getName()
        ));
        return restaurantService.updateRestaurantByName(updateRestaurantNameDto, userId);
    }
}
