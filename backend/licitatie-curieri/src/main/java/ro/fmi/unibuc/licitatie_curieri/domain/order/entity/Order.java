package ro.fmi.unibuc.licitatie_curieri.domain.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.User;

import java.time.Instant;
import java.util.ArrayList;
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

    @Column(name = "auction_deadline")
    private Instant auctionDeadline;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private User courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<OrderMenuItemAssociation> orderMenuItemAssociations = new ArrayList<>();
}
