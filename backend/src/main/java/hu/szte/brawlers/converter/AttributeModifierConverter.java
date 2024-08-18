package hu.szte.brawlers.converter;

import hu.szte.brawlers.model.AttributeModifier;
import hu.szte.brawlers.model.dto.AttributeModifierDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttributeModifierConverter {
    public AttributeModifier dtoToAttributeModifier(AttributeModifierDto attributeModifierDto) {
        return AttributeModifier.builder()
                .dexterityModifier(attributeModifierDto.getDexterityModifier())
                .enduranceModifier(attributeModifierDto.getEnduranceModifier())
                .luckModifier(attributeModifierDto.getLuckModifier())
                .maxHpModifier(attributeModifierDto.getMaxHpModifier())
                .intelligenceModifier(attributeModifierDto.getIntelligenceModifier())
                .build();
    }

    public AttributeModifierDto attributeModifierToDto(AttributeModifier attributeModifier) {
        return AttributeModifierDto.builder()
                .dexterityModifier(attributeModifier.getDexterityModifier())
                .enduranceModifier(attributeModifier.getEnduranceModifier())
                .luckModifier(attributeModifier.getLuckModifier())
                .maxHpModifier(attributeModifier.getMaxHpModifier())
                .intelligenceModifier(attributeModifier.getIntelligenceModifier())
                .build();
    }

    public AttributeModifier updateAttributeModifier(AttributeModifier attributeModifier, AttributeModifierDto attributeModifierDto) {
        attributeModifier.setDexterityModifier(attributeModifierDto.getDexterityModifier());
        attributeModifier.setEnduranceModifier(attributeModifierDto.getEnduranceModifier());
        attributeModifier.setLuckModifier(attributeModifierDto.getLuckModifier());
        attributeModifier.setIntelligenceModifier(attributeModifierDto.getIntelligenceModifier());
        attributeModifier.setMaxHpModifier(attributeModifierDto.getMaxHpModifier());
        return attributeModifier;
    }


}
