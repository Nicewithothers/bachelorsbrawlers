package hu.szte.brawlers.repository;

import hu.szte.brawlers.model.BoosterItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TobaccoShopRepository extends JpaRepository<BoosterItem, Long> {
    Optional<BoosterItem> getBoosterItemByName(String name);
}
