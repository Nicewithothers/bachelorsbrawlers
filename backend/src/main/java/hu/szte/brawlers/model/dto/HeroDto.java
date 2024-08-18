package hu.szte.brawlers.model.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroDto {
    private String name;
    private Integer maxHp;
    private Integer intelligence;
    private Integer luck;
    private Integer endurance;
    private Integer dexterity;
    private Integer diligence;
    private Integer adventure;
    private Long xp;
    private Long maxXp;
    private Integer level;
    private Integer forint;
    private Integer crypto;
    @Nullable
    private BoosterItemDto boosterItem;
    @Nullable
    private List<ItemDto> backpackItems;
    @Nullable
    private List<ItemDto> equipmentItems;
    private String profileName;
    private Integer dungeonLevel;
    private String image;
    private byte[] picture;
    @Nullable
    private LocalDateTime nextDungeonTry;
}
