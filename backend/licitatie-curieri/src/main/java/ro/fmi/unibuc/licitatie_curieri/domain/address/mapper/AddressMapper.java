package ro.fmi.unibuc.licitatie_curieri.domain.address.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.AddressCreationDto;
import org.openapitools.model.AddressCreationResponseDto;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    Address mapToAddress(AddressCreationDto addressCreationDto);

    AddressCreationResponseDto mapToAddressCreationResponseDto(Address address);
}
