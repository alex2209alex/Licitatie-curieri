package ro.fmi.unibuc.licitatie_curieri.domain.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.fmi.unibuc.licitatie_curieri.domain.menuitem.entity.MenuItem;

@Entity
@Table(name = "order_menu_item_associations")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderMenuItemAssociation {
    @EmbeddedId
    private OrderMenuItemAssociationId id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", insertable = false, updatable = false)
    private MenuItem menuItem;
}
