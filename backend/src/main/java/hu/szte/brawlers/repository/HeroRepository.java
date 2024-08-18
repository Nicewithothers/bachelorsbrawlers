package hu.szte.brawlers.repository;

import hu.szte.brawlers.model.Hero;
import hu.szte.brawlers.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {
    List<Hero> findAllByOrderByXpDesc();

    Optional<Hero> getHeroByName(String name);

    boolean deleteHeroByName(String name);

    Optional<Hero> getHeroByProfile(Profile profileName);

    List<Hero> findAllByIdIn(List<String> ids);
}
