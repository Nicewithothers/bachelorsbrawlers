package hu.szte.brawlers.model.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FightDto {
    private String name;
    private Integer maxHp;
    private Integer intelligence;
    private Integer luck;
    private Integer endurance;
    private Integer dexterity;
    private Integer diligence;
    @Nullable
    private List<ItemDto> backpackItems;
    @Nullable
    private List<ItemDto> equipmentItems;
    private String profileName;
    private String MonsterName;
}
