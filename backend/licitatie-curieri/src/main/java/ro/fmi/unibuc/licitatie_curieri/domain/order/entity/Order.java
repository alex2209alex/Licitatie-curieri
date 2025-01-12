package ro.fmi.unibuc.licitatie_curieri.domain.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
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

    @Column(name = "food_price")
    private Double foodPrice;

    @Column(name = "delivery_price")
    private Double deliveryPrice;

    @Column(name = "delivery_price_limit")
    private Double deliveryPriceLimit;

    @Column(name = "auction_deadline")
    private Instant auctionDeadline;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private User courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<OrderMenuItemAssociation> orderMenuItemAssociations = new ArrayList<>();
}
