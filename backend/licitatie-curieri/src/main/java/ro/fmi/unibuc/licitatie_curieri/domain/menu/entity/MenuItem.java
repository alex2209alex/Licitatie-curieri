package ro.fmi.unibuc.licitatie_curieri.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.entity.Restaurant;

@Entity
@Table(name = "menu_items")
@Data
@NoArgsConstructor
public class MenuItem {
    @Id
    @SequenceGenerator(name = "menus_gen", sequenceName = "menus_seq", allocationSize = 20)
    @GeneratedValue(generator = "menus_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "ingredients_list")
    private String ingredientsList;

    @Column(name = "photo")
    private String photo;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "was_removed")
    private Boolean wasRemoved;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
