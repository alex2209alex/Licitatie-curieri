package ro.fmi.unibuc.licitatie_curieri.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;

@Entity
@Table(name = "user_address_associations")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserAddressAssociation {
    @EmbeddedId
    private UserAddressAssociationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", insertable = false, updatable = false)
    private Address address;
}
