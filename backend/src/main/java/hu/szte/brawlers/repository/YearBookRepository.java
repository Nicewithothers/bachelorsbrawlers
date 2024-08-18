package hu.szte.brawlers.repository;

import hu.szte.brawlers.model.YearBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YearBookRepository extends JpaRepository<YearBookEntity, Long> {
}
