package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.RestaurantApi;
import org.openapitools.model.RestaurantDetailsDto;
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
        log.info(String.format(LogMessageUtils.GET_RESTAURANTS, addressId));
        return restaurantService.getRestaurants(addressId);
    }
}
