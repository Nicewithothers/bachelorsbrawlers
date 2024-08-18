
package hu.szte.brawlers.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questId;
    @ManyToOne
    @Nullable
    private Item reward;
    private Long xp;
    @Enumerated(EnumType.STRING)
    private GruntName monsterType;
    private Integer cost;
    @ManyToOne
    private Hero hero;
    private String fileName;
    private String monsterName;
}
