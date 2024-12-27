package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openapitools.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.mapper.RestaurantMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.repository.RestaurantRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociation;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;
import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    private static final double SEARCH_RANGE = 10;
    private static final double EARTH_RADIUS = 6371;

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final AddressService addressService;
    private final UserAddressAssociationService userAddressAssociationService;

    @Transactional(readOnly = true)
    public List<RestaurantDetailsDto> getRestaurants(Long addressId, String email) {
        val user = userRepository.findByEmail(email).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }
        if (user.getUserType() != UserType.CLIENT && user.getUserType() != UserType.ADMIN_RESTAURANT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_AND_ADMIN_REST_CAN_GET_RESTAURANTS);
        }

        val address = user.getUserAddressAssociations().stream()
                .filter(userAddressAssociation -> userAddressAssociation.getId().getAddressId().equals(addressId))
                .map(UserAddressAssociation::getAddress)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.USER_ADDRESS_NOT_FOUND, addressId)));

        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toRestaurantDetailsDto)
                .filter(restaurantDetailsDto -> isWithinRange(restaurantDetailsDto, address))
                .toList();
    }

    @Transactional
    public CreateRestaurantResponseDto createRestaurant(CreateRestaurantDto createRestaurantDto, String email) {
        val user = userRepository.findByEmail(email).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_ADMIN_REST_CAN_CREATE_RESTAURANTS);
        }

        restaurantRepository.findByName(createRestaurantDto.getName())
                .ifPresent(restaurant -> {
                    throw new BadRequestException(
                        String.format(ErrorMessageUtils.RESTAURANT_ALREADY_EXISTS,
                                createRestaurantDto.getName())
                    );
                });

        Long addressId = addressService.createAddress(
                restaurantMapper.toAddressCreationDto(createRestaurantDto),
                email
        ).getId();

        val restaurant = restaurantRepository.save(restaurantMapper.toRestaurant(createRestaurantDto, addressId));

        return restaurantMapper.toCreateRestaurantResponseDto(restaurant);
    }

    @Transactional
    public void deleteRestaurant(Long restaurantId, String email) {
        val user = userRepository.findByEmail(email).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_ADMIN_REST_CAN_DELETE_RESTAURANTS);
        }

        val restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.RESTAURANT_NOT_FOUND, restaurantId)));

        restaurantRepository.delete(restaurant);
        userAddressAssociationService.deleteUserAddressAssociationByAddressId(restaurant.getAddress().getId());
        addressService.deleteAddress(restaurant.getAddress().getId());
    }

    @Transactional
    public UpdateRestaurantNameResponseDto updateRestaurantByName(UpdateRestaurantNameDto updateRestaurantNameDto, String email) {
        val user = userRepository.findByEmail(email).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if(user.getUserType() != UserType.ADMIN_RESTAURANT) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_ADMIN_REST_CAN_UPDATE_RESTAURANTS);
        }

        val restaurant = restaurantRepository.findById(updateRestaurantNameDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.RESTAURANT_NOT_FOUND, updateRestaurantNameDto.getId())));

        restaurant.setName(updateRestaurantNameDto.getName());
        restaurantRepository.save(restaurant);

        return restaurantMapper.toUpdateRestaurantNameResponseDto(restaurant);
    }

    private boolean isWithinRange(RestaurantDetailsDto restaurantDetailsDto, Address address) {
        return SEARCH_RANGE >= calculateDistance(restaurantDetailsDto.getLatitude(), restaurantDetailsDto.getLongitude(), address.getLatitude(), address.getLongitude());
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
}
