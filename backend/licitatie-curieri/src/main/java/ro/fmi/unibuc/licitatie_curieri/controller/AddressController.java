package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.address.api.AddressApi;
import ro.fmi.unibuc.licitatie_curieri.controller.address.models.AddressCreationDto;
import ro.fmi.unibuc.licitatie_curieri.controller.address.models.AddressCreationResponseDto;
import ro.fmi.unibuc.licitatie_curieri.controller.address.models.AddressDetailsDto;
import ro.fmi.unibuc.licitatie_curieri.service.AddressService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AddressController implements AddressApi {
    private final AddressService addressService;

    @Override
    public List<AddressDetailsDto> getAddresses() {
        log.info(LogMessageUtils.GET_ADDRESSES);

        return addressService.getAddresses();
    }

    @Override
    public AddressCreationResponseDto createAddress(@RequestBody AddressCreationDto addressCreationDto) {
        log.info(String.format(LogMessageUtils.CREATE_ADDRESS, addressCreationDto.getLatitude(), addressCreationDto.getLongitude()));

        return addressService.createAddress(addressCreationDto);
    }
}
