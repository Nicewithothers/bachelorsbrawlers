package hu.szte.brawlers.service;

import hu.szte.brawlers.model.AttributeModifier;
import hu.szte.brawlers.model.Damage;
import hu.szte.brawlers.model.Item;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CombatUtilService {

    public Damage calculateDamage(int intelligence, int luck, int dexterity, @Nullable List<Item> equipmentItems) {
        // Számítás a szerencsés találat esetén
        int damage = intelligence;
        boolean isCrit = false;
        int luckyNum = (int) (Math.random() * 100);
        if (luckyNum >= 90) {
            damage += (int) (damage * luck * 0.25);
            isCrit = true;
        }

        // Equipment itemek hatása a sebzésre
        if (equipmentItems != null) {
            for (Item item : equipmentItems) {
                damage += getBonus(item);
            }
        }

        // Dexterity és bónusz ügyesség által okozott csökkentés
        int dodgeChance = (int) (Math.random() * 100);
        if (dodgeChance >= 90) {
            return new Damage(isCrit, 0); // A támadás kitért
        } else {
            return new Damage(isCrit, Math.max(damage - dexterity, 1)); // Csökkentett sebzés a dexterity és bónusz ügyesség alapján
        }
    }

    private Integer getBonus(Item item) {
        int bonus = 0;
        if (item.getAttributeModifiers() != null) {
            AttributeModifier modifier = item.getAttributeModifiers();

            if (modifier.getIntelligenceModifier() != null) {
                bonus += modifier.getIntelligenceModifier();
            }
            if (modifier.getLuckModifier() != null) {
                bonus += modifier.getLuckModifier();
            }

        }
        return bonus;
    }
}
