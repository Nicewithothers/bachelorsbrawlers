package hu.szte.brawlers.model;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Hero extends BaseEntity {
    @OneToOne
    private Profile profile;
    private Integer diligence;
    private Integer adventure = 100;
    private Long xp = 0L;
    private Long maxXp = 100L;
    private Integer level = 1;
    private Integer forint = 5000;
    private Integer crypto = 10;
    private Integer lastWin;
    private Integer dungeonLevel=1;
    @Nullable
    private LocalDateTime nextDungeonTry= LocalDateTime.now();
    @OneToOne
    private BoosterItem boosterItem;
    @ManyToMany
    private List<Item> equipmentItems = new ArrayList<>();
    @ManyToMany
    private List<Item> backpackItems = new ArrayList<>();
    private String image;
}
