package hu.szte.brawlers.repository;

import hu.szte.brawlers.model.Monster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonsterRepository extends JpaRepository<Monster, Long> {
    Long deleteByName(String name);
    Optional<Monster> getMonsterByName(String monsterName);
    Optional<Monster> findTopByOrderByDungeonLevelDesc();
}

