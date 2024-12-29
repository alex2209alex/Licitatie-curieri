package ro.fmi.unibuc.licitatie_curieri.domain.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.MenuItem;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.User;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @SequenceGenerator(name = "orders_gen", sequenceName = "orders_seq", allocationSize = 20)
    @GeneratedValue(generator = "orders_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "licitation_deadline", nullable = false)
    private Instant liciationDeadline;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private User courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "menu_id")
    private List<MenuItem> orderList; // IDs of menu items
}
