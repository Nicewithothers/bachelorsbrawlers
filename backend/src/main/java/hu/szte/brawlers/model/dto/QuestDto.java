package hu.szte.brawlers.model.dto;

import hu.szte.brawlers.model.GruntName;
import lombok.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class QuestDto {
    private ItemDto reward;
    private Long xp;
    private GruntName monsterType;
    private Integer cost;
    private HeroDto hero;
    private String fileName;
    private String monsterName;
    private byte[] image;
}
