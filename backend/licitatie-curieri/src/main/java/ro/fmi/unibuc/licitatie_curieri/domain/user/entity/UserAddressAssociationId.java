package ro.fmi.unibuc.licitatie_curieri.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressAssociationId {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "address_id")
    private Long addressId;
}
