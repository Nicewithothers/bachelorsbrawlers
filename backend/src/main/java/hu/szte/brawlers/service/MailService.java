package hu.szte.brawlers.service;

import hu.szte.brawlers.converter.MailConverter;
import hu.szte.brawlers.model.Mail;
import hu.szte.brawlers.model.dto.MailDto;
import hu.szte.brawlers.repository.MailRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailRepository mailRepository;
    private final MailConverter mailConverter;

    @Transactional
    public void addMail(String heroName, String attacker, boolean victory, Integer reward, String subject) {
        Mail mail = Mail.builder()
                .heroName(heroName)
                .attacker(attacker)
                .victory(victory)
                .reward(reward)
                .seen(false)
                .created(LocalDateTime.now())
                .subject(subject)
                .build();

        mailRepository.save(mail);
    }

    public List<MailDto> getMails(String heroName) {
        List<Mail> mails = mailRepository.findAllByHeroName(heroName);

        List<MailDto> mailDtos = mails.stream().map(mailConverter::mailToDto).toList();


        mails.forEach(mail -> mail.setSeen(true));
        mailRepository.saveAll(mails);

        return mailDtos;
    }


    public Integer getUnread(String heroName) {
        return mailRepository.countByHeroNameAndSeenIsFalse(heroName);
    }

}
