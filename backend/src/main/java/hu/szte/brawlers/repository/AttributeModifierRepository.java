package hu.szte.brawlers.repository;

import hu.szte.brawlers.model.AttributeModifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeModifierRepository extends JpaRepository<AttributeModifier, Long> { }
