package hu.szte.brawlers.model;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Monster extends BaseEntity {
    private Integer damageRange;
    private String image;
    private Integer dungeonLevel;
    private String motto;
}
