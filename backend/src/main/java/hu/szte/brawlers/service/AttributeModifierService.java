package hu.szte.brawlers.service;

import hu.szte.brawlers.converter.AttributeModifierConverter;
import hu.szte.brawlers.model.AttributeModifier;
import hu.szte.brawlers.model.Hero;
import hu.szte.brawlers.model.dto.AttributeModifierDto;
import hu.szte.brawlers.repository.AttributeModifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeModifierService {
    private final AttributeModifierRepository attributeModifierRepository;
    private final AttributeModifierConverter attributeModifierConverter;

    public List<AttributeModifierDto> findAll() {
        return attributeModifierRepository.findAll().stream().map(attributeModifierConverter::attributeModifierToDto).collect(Collectors.toList());
    }

    public AttributeModifierDto getAttributeModifierById(Long id) {
        return attributeModifierConverter.attributeModifierToDto(attributeModifierRepository.findById(id).orElseThrow());
    }

    public void deleteAttributeModifier(Long id) {
        attributeModifierRepository.deleteById(id);
    }

    public AttributeModifierDto addAttributeModifier(AttributeModifierDto attributeModifierDto) {
        return attributeModifierConverter.attributeModifierToDto(attributeModifierRepository.save(attributeModifierConverter.dtoToAttributeModifier(attributeModifierDto)));
    }

    public AttributeModifierDto updateAttributeModifier(AttributeModifierDto attributeModifierDto, Long id) {
        AttributeModifier attributeModifier = attributeModifierRepository.findById(id).orElseThrow();
        return attributeModifierConverter.attributeModifierToDto(attributeModifierRepository.save(attributeModifierConverter.updateAttributeModifier(attributeModifier, attributeModifierDto)));
    }

//    public void applyAttributeModifiers(Hero hero, List<AttributeModifier> attributeModifiers) {
//        for (AttributeModifier modifier : attributeModifiers) {
//            int maxHpModifier = Objects.requireNonNullElse(modifier.getMaxHpModifier(), 0);
//            int intelligenceModifier = Objects.requireNonNullElse(modifier.getIntelligenceModifier(), 0);
//            int luckModifier = Objects.requireNonNullElse(modifier.getLuckModifier(), 0);
//            int enduranceModifier = Objects.requireNonNullElse(modifier.getEnduranceModifier(), 0);
//            int dexterityModifier = Objects.requireNonNullElse(modifier.getDexterityModifier(), 0);
//
//            hero.setMaxHp(hero.getMaxHp() + maxHpModifier);
//            hero.setIntelligence(hero.getIntelligence() + intelligenceModifier);
//            hero.setLuck(hero.getLuck() + luckModifier);
//            hero.setEndurance(hero.getEndurance() + enduranceModifier);
//            hero.setDexterity(hero.getDexterity() + dexterityModifier);
//        }
//    }
//
//    public void revertAttributeModifiers(Hero hero, List<AttributeModifier> attributeModifiers) {
//        for (AttributeModifier modifier : attributeModifiers) {
//            int maxHpModifier = Objects.requireNonNullElse(modifier.getMaxHpModifier(), 0);
//            int intelligenceModifier = Objects.requireNonNullElse(modifier.getIntelligenceModifier(), 0);
//            int luckModifier = Objects.requireNonNullElse(modifier.getLuckModifier(), 0);
//            int enduranceModifier = Objects.requireNonNullElse(modifier.getEnduranceModifier(), 0);
//            int dexterityModifier = Objects.requireNonNullElse(modifier.getDexterityModifier(), 0);
//
//            hero.setMaxHp(hero.getMaxHp() - maxHpModifier);
//            hero.setIntelligence(hero.getIntelligence() - intelligenceModifier);
//            hero.setLuck(hero.getLuck() - luckModifier);
//            hero.setEndurance(hero.getEndurance() - enduranceModifier);
//            hero.setDexterity(hero.getDexterity() - dexterityModifier);
//        }
//    }
}
