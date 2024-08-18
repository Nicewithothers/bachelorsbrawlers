package hu.szte.brawlers.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AttributeModifierDto {
    private Integer maxHpModifier;
    private Integer intelligenceModifier;
    private Integer luckModifier;
    private Integer enduranceModifier;
    private Integer dexterityModifier;
}
