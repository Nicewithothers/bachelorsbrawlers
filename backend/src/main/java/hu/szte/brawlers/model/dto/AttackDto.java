package hu.szte.brawlers.model.dto;

import hu.szte.brawlers.model.Damage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AttackDto extends Damage {
    private boolean heroAttack;

    public AttackDto(Damage damage, boolean heroAttack) {
        super(damage.isCrit(), damage.getValue());
        this.heroAttack = heroAttack;
    }
}
