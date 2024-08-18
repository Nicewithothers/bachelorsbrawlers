package hu.szte.brawlers.repository;

import hu.szte.brawlers.model.Item;
import hu.szte.brawlers.model.Rarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item>getItemByName(String name);

    Optional<Item>getItemByRarity(Rarity rarity);

    Long deleteByName(String name);

    Optional<List<Item>>getItemsByRarity(Rarity rarity);
}
