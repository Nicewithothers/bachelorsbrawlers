package hu.szte.brawlers.converter;

import hu.szte.brawlers.model.Mail;
import hu.szte.brawlers.model.dto.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class MailConverter {
    public MailDto mailToDto(Mail mail){
        return MailDto.builder()
                .message(String.format("%s megtámadott téged. Eredmény: %s. %s %d forintot.",
                        mail.getAttacker(),
                        mail.isVictory() ? "GYŐZELEM" : "VERESÉG",
                        mail.isVictory() ? "Nyertél" : "Vesztettél",
                        mail.getReward()))
                .created(mail.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .seen(mail.isSeen())
                .subject(mail.getSubject())
                .sender("System")
                .build();
    }
}
