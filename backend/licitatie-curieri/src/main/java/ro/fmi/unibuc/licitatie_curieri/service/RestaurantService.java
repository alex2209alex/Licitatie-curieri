package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.restaurant.models.*;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
import ro.fmi.unibuc.licitatie_curieri.domain.address.repository.AddressRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.mapper.RestaurantMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.repository.RestaurantRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociation;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    private static final double SEARCH_RANGE = 10;
    private static final double EARTH_RADIUS = 6371;

    private final AddressRepository addressRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final UserInformationService userInformationService;

    @Transactional(readOnly = true)
    public List<RestaurantDetailsDto> getRestaurants(Long addressId) {
        userInformationService.ensureCurrentUserIsVerified();
        if (!userInformationService.isCurrentUserClient() && !userInformationService.isCurrentUserRestaurantAdmin()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_AND_RESTAURANT_ADMIN_CAN_GET_RESTAURANTS);
        }

        if (userInformationService.isCurrentUserClient()) {
            if (addressId == null) {
                throw new BadRequestException(ErrorMessageUtils.MISSING_ADDRESS_ID);
            }

            val address = userInformationService.getCurrentUser().getUserAddressAssociations().stream()
                    .filter(userAddressAssociation -> userAddressAssociation.getId().getAddressId().equals(addressId))
                    .map(UserAddressAssociation::getAddress)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.USER_ADDRESS_WITH_ID_NOT_FOUND, addressId)));

            return restaurantRepository.findAll().stream()
                    .map(restaurantMapper::toRestaurantDetailsDto)
                    .filter(restaurantDetailsDto -> isWithinRange(restaurantDetailsDto, address))
                    .toList();
        }

        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toRestaurantDetailsDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RestaurantDetailsDto getRestaurant(Long restaurantId) {
        ensureCurrentUserIsVerifiedRestaurantAdmin(ErrorMessageUtils.ONLY_RESTAURANT_ADMIN_CAN_GET_RESTAURANT);

        val restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.RESTAURANT_NOT_FOUND, restaurantId)));

        return restaurantMapper.toRestaurantDetailsDto(restaurant);
    }

    @Transactional
    public RestaurantCreationResponseDto createRestaurant(RestaurantCreationDto restaurantCreationDto) {
        ensureCurrentUserIsVerifiedRestaurantAdmin(ErrorMessageUtils.ONLY_RESTAURANT_ADMIN_CAN_CREATE_RESTAURANTS);

        val address = addressRepository.save(restaurantMapper.toAddress(restaurantCreationDto));
        val persistedRestaurant = restaurantRepository.save(restaurantMapper.toRestaurant(restaurantCreationDto, address));

        return restaurantMapper.toRestaurantCreationResponseDto(persistedRestaurant);
    }

    @Transactional
    public RestaurantUpdateResponseDto updateRestaurant(Long restaurantId, RestaurantUpdateDto restaurantUpdateDto) {
        ensureCurrentUserIsVerifiedRestaurantAdmin(ErrorMessageUtils.ONLY_RESTAURANT_ADMIN_CAN_UPDATE_RESTAURANTS);

        val restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.RESTAURANT_NOT_FOUND, restaurantId)));

        restaurant.setName(restaurantUpdateDto.getName());
        restaurantRepository.save(restaurant);

        return restaurantMapper.toRestaurantUpdateResponseDto(restaurant);
    }

    @Transactional
    public void removeRestaurant(Long restaurantId) {
        ensureCurrentUserIsVerifiedRestaurantAdmin(ErrorMessageUtils.ONLY_RESTAURANT_ADMIN_CAN_REMOVE_RESTAURANTS);

        val restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.RESTAURANT_NOT_FOUND, restaurantId)));

        restaurant.setWasRemoved(true);

        restaurantRepository.save(restaurant);
    }

    private boolean isWithinRange(RestaurantDetailsDto restaurantDetailsDto, Address address) {
        return SEARCH_RANGE >= calculateDistance(restaurantDetailsDto.getAddress().getLatitude(), restaurantDetailsDto.getAddress().getLongitude(), address.getLatitude(), address.getLongitude());
    }

    // Source https://www.baeldung.com/java-find-distance-between-points
    private double calculateDistance(double startLat, double startLong, double endLat, double endLong) {
        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    private void ensureCurrentUserIsVerifiedRestaurantAdmin(String errorMessage) {
        userInformationService.ensureCurrentUserIsVerified();
        if (!userInformationService.isCurrentUserRestaurantAdmin()) {
            throw new ForbiddenException(errorMessage);
        }
    }
}
