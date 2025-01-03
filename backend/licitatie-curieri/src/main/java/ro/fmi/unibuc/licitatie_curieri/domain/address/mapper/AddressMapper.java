package ro.fmi.unibuc.licitatie_curieri.domain.address.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ro.fmi.unibuc.licitatie_curieri.controller.address.models.AddressCreationDto;
import ro.fmi.unibuc.licitatie_curieri.controller.address.models.AddressCreationResponseDto;
import ro.fmi.unibuc.licitatie_curieri.controller.address.models.AddressDetailsDto;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AddressMapper {
    AddressDetailsDto mapToAddressDetailsDto(Address address);

    @Mapping(target = "id", ignore = true)
    Address mapToAddress(AddressCreationDto addressCreationDto);

    AddressCreationResponseDto mapToAddressCreationResponseDto(Address address);
}
