package hu.szte.brawlers.repository;

import hu.szte.brawlers.model.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {
    List<Mail> findAllByHeroName(String heroName);
    Integer countByHeroNameAndSeenIsFalse(String heroName);


}
