package ro.fmi.unibuc.licitatie_curieri.domain.address.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
public class Address {
    @Id
    @SequenceGenerator(name = "addresses_gen", sequenceName = "addresses_seq", allocationSize = 20)
    @GeneratedValue(generator = "addresses_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "details")
    private String details;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}
