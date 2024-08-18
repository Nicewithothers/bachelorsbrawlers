package hu.szte.brawlers.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grunt {
    private GruntName name;
    private Integer maxHp;
    private Integer intelligence;
    private Integer luck;
    private Integer endurance;
    private Integer dexterity;
}
