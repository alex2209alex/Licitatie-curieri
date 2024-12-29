package ro.fmi.unibuc.licitatie_curieri.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
