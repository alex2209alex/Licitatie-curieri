package ro.fmi.unibuc.licitatie_curieri.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.menu.entity.Menu;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByNameAndIngredientsList(String name, String ingredientsList);

    Optional<Menu> findById(Long id);
}
