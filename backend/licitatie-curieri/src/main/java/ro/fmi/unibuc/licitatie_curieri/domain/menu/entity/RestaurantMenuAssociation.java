package ro.fmi.unibuc.licitatie_curieri.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapping;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;

@Entity
@Table(name = "restaurant_menu_associations")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RestaurantMenuAssociation {
    @EmbeddedId
    private RestaurantMenuAssociationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private Menu menu;
}
