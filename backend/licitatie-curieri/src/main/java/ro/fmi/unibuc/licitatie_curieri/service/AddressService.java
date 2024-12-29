package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.address.models.AddressCreationDto;
import ro.fmi.unibuc.licitatie_curieri.controller.address.models.AddressCreationResponseDto;
import ro.fmi.unibuc.licitatie_curieri.controller.address.models.AddressDetailsDto;
import ro.fmi.unibuc.licitatie_curieri.domain.address.mapper.AddressMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.address.repository.AddressRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociation;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociationId;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserInformationService userInformationService;

    @Transactional(readOnly = true)
    public List<AddressDetailsDto> getAddresses() {
        userInformationService.ensureCurrentUserIsVerified();
        if (!userInformationService.isCurrentUserClient()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_CAN_GET_ADDRESSES);
        }

        return userInformationService.getCurrentUser().getUserAddressAssociations().stream()
                .map(UserAddressAssociation::getAddress)
                .map(addressMapper::mapToAddressDetailsDto)
                .toList();
    }

    @Transactional
    public AddressCreationResponseDto createAddress(AddressCreationDto addressCreationDto) {
        userInformationService.ensureCurrentUserIsVerified();
        if (!userInformationService.isCurrentUserClient()) {
            throw new ForbiddenException(ErrorMessageUtils.ONLY_CLIENT_CAN_CREATE_ADDRESSES);
        }

        val persistedAddress = addressRepository.save(addressMapper.mapToAddress(addressCreationDto));

        val user = userInformationService.getCurrentUser();
        val userAddressAssociationId = new UserAddressAssociationId(user.getId(), persistedAddress.getId());
        val userAddressAssociation = new UserAddressAssociation();
        userAddressAssociation.setId(userAddressAssociationId);
        user.getUserAddressAssociations().add(userAddressAssociation);

        return addressMapper.mapToAddressCreationResponseDto(persistedAddress);
    }
}
