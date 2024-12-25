package ro.fmi.unibuc.licitatie_curieri.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menus")
@Data
@NoArgsConstructor
public class Menu {
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
}
