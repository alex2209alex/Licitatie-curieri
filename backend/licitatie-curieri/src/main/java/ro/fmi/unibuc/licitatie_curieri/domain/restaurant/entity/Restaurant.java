package ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.MenuItem;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
public class Restaurant {
    @Id
    @SequenceGenerator(name = "restaurants_gen", sequenceName = "restaurants_seq", allocationSize = 20)
    @GeneratedValue(generator = "restaurants_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private List<MenuItem> menuItems = new ArrayList<>();
}
