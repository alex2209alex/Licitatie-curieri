package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openapitools.model.AddressCreationResponseDto;
import org.openapitools.model.CreateRestaurantDto;
import org.openapitools.model.CreateRestaurantResponseDto;
import org.openapitools.model.RestaurantDetailsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
import ro.fmi.unibuc.licitatie_curieri.domain.address.repository.AddressRepository;
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

    @Transactional(readOnly = true)
    public List<RestaurantDetailsDto> getRestaurants(Long addressId) {
        val user = userRepository.findById(1L).get(); // TODO user needs to be retrieved from security context or some service class

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }
        if (UserType.CLIENT != user.getUserType()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_CAN_GET_RESTAURANTS);
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
    public CreateRestaurantResponseDto createRestaurant(CreateRestaurantDto createRestaurantDto) {
        // TODO: only RESTAURANT_ADMIN can create restaurants. To be modified later
        restaurantRepository.findByName(createRestaurantDto.getName())
                .ifPresent(restaurant -> {
                    throw new BadRequestException(
                        String.format(ErrorMessageUtils.RESTAURANT_ALREADY_EXISTS,
                                createRestaurantDto.getName())
                    );
                });

        Long addressId = addressService.createAddress(
                restaurantMapper.toAddressCreationDto(createRestaurantDto)
        ).getId();

        val restaurant = restaurantRepository.save(restaurantMapper.toRestaurant(createRestaurantDto, addressId));

        return restaurantMapper.toCreateRestaurantResponseDto(restaurant);
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
