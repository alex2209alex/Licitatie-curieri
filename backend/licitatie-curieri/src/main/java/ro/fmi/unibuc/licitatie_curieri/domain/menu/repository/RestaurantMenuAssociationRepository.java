package ro.fmi.unibuc.licitatie_curieri.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.RestaurantMenuAssociation;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.RestaurantMenuAssociationId;

import java.util.List;
import java.util.Optional;

public interface RestaurantMenuAssociationRepository extends JpaRepository<RestaurantMenuAssociation, RestaurantMenuAssociationId> {
    @Query("SELECT rma FROM RestaurantMenuAssociation rma WHERE rma.id.menuId = :menuId")
    List<RestaurantMenuAssociation> findByMenuId(@Param("menuId") Long menuId);

    Optional<RestaurantMenuAssociation> findByRestaurantIdAndMenuId(Long restaurantId, Long menuId);

    @Query("SELECT rma FROM RestaurantMenuAssociation rma WHERE rma.id.restaurantId = :restaurantId")
    List<RestaurantMenuAssociation> findByRestaurantId(@Param("restaurantId") Long restaurantId);
}