package ro.fmi.unibuc.licitatie_curieri.domain.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
