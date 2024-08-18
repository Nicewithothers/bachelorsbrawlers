package hu.szte.brawlers.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MailDto {
    private String subject;
    private String sender;
    private String message;
    private String created;
    private boolean seen;

}
