package ro.fmi.unibuc.licitatie_curieri.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociation;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociationId;

public interface UserAddressAssociationRepository extends JpaRepository<UserAddressAssociation, UserAddressAssociationId> {
}
