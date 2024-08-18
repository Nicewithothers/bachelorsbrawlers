package hu.szte.brawlers.repository;

import hu.szte.brawlers.model.Dummy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DummyRepository extends JpaRepository<Dummy, Long> { }
