package ro.fmi.unibuc.licitatie_curieri.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociation;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociationId;

public interface UserAddressAssociationRepository extends JpaRepository<UserAddressAssociation, UserAddressAssociationId> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserAddressAssociation uaa WHERE uaa.id.addressId = :addressId")
    void deleteByAddressId(Long addressId);
}
