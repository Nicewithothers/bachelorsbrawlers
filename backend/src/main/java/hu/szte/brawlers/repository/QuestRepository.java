package hu.szte.brawlers.repository;

import hu.szte.brawlers.model.GruntName;
import hu.szte.brawlers.model.Hero;
import hu.szte.brawlers.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
    List<Quest> findAllByHeroName(String heroName);
    Optional<Quest> getByMonsterTypeAndHero(GruntName monsterName, Hero hero);
    int deleteAllByHeroName(String heroName);
}
