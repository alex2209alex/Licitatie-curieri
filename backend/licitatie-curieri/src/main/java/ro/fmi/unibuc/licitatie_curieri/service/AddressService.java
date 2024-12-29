package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.openapitools.model.AddressCreationDto;
import org.openapitools.model.AddressCreationResponseDto;
import org.openapitools.model.AddressDetailsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.domain.address.mapper.AddressMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.address.repository.AddressRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociation;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociationId;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;
import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Transactional(readOnly = true)
    public List<AddressDetailsDto> getAddresses(String email) {
        val user = userRepository.findByEmail(email).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }
        if (UserType.CLIENT != user.getUserType()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_CAN_GET_ADDRESSES);
        }

        return user.getUserAddressAssociations().stream()
                .map(UserAddressAssociation::getAddress)
                .map(addressMapper::mapToAddressDetailsDto)
                .toList();
    }

    @Transactional
    public AddressCreationResponseDto createAddress(AddressCreationDto addressCreationDto, String email) {
        val user = userRepository.findByEmail(email).get();

        if (!user.isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }

        if (UserType.CLIENT != user.getUserType() &&
        UserType.ADMIN_RESTAURANT != user.getUserType()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_AND_ADMIN_REST_CAN_CREATE_ADDRESS);
        }

        val address = addressRepository.save(addressMapper.mapToAddress(addressCreationDto));

        val userAddressAssociationId = new UserAddressAssociationId(user.getId(), address.getId());
        val userAddressAssociation = new UserAddressAssociation();
        userAddressAssociation.setId(userAddressAssociationId);
        user.getUserAddressAssociations().add(userAddressAssociation);

        return addressMapper.mapToAddressCreationResponseDto(address);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        val address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.ADDRESS_NOT_FOUND, addressId)));

        addressRepository.delete(address);
    }
}
