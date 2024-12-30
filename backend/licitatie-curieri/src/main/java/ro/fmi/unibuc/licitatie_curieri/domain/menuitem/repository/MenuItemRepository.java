package ro.fmi.unibuc.licitatie_curieri.domain.menuitem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.fmi.unibuc.licitatie_curieri.domain.menuitem.entity.MenuItem;

import java.util.List;
import java.util.Set;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @Query("SELECT mi FROM MenuItem mi WHERE mi.id IN :ids AND mi.restaurant.id = :restaurantId")
    List<MenuItem> findAllByIdsAndRestaurantId(Set<Long> ids, Long restaurantId);
}
