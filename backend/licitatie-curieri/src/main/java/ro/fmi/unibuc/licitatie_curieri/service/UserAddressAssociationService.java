package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserAddressAssociationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAddressAssociationService {
    private final UserAddressAssociationRepository userAddressAssociationRepository;

    @Transactional
    public void deleteUserAddressAssociationByAddressId(Long addressId) {
        userAddressAssociationRepository.deleteByAddressId(addressId);
    }
}
