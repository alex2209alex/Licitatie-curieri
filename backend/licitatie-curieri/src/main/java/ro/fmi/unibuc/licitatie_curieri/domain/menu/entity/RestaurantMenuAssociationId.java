package ro.fmi.unibuc.licitatie_curieri.domain.menu.entity;

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
public class RestaurantMenuAssociationId {
    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "menu_id")
    private Long menuId;
}
