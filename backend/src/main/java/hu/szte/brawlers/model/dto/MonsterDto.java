package hu.szte.brawlers.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonsterDto {
    private String name;
    private Integer maxHp;
    private Integer intelligence;
    private Integer luck;
    private Integer endurance;
    private Integer dexterity;
    private Integer damageRange;
    private String image;
    private byte[] picture;
    private Integer dungeonLevel;
    private String motto;
    //...
}
