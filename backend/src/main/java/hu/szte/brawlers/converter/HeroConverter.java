package hu.szte.brawlers.converter;

import hu.szte.brawlers.model.Hero;
import hu.szte.brawlers.model.Profile;
import hu.szte.brawlers.model.dto.HeroDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HeroConverter {
    private final ItemConverter itemConverter;
    private final BoosterItemConverter boosterItemConverter;

    public Hero dtoToHero(HeroDto heroDto) {
        Hero hero = new Hero();
        hero.setName(heroDto.getName());
        hero.setMaxHp(heroDto.getMaxHp());
        hero.setIntelligence(heroDto.getIntelligence());
        hero.setLuck(heroDto.getLuck());
        hero.setEndurance(heroDto.getEndurance());
        hero.setDexterity(heroDto.getDexterity());
        hero.setDiligence(heroDto.getDiligence());
        hero.setAdventure(heroDto.getAdventure());
        hero.setForint(heroDto.getForint());
        hero.setCrypto(heroDto.getCrypto());
        hero.setXp(heroDto.getXp());
        hero.setMaxXp(heroDto.getMaxXp());
        hero.setLevel(heroDto.getLevel());
        hero.setBoosterItem(heroDto.getBoosterItem() != null ? boosterItemConverter.dtoToBoosterItem(heroDto.getBoosterItem()) : null);
        hero.setBackpackItems(heroDto.getBackpackItems() != null ? heroDto.getBackpackItems().stream().map(itemConverter::dtoToItem).toList() : null);
        hero.setEquipmentItems(heroDto.getEquipmentItems() != null ? heroDto.getEquipmentItems().stream().map(itemConverter::dtoToItem).toList() : null);
        hero.setDungeonLevel(heroDto.getDungeonLevel());
        hero.setNextDungeonTry(heroDto.getNextDungeonTry());
        hero.setImage(heroDto.getImage());
        return hero;
    }

    public Hero updateHero(HeroDto heroDto, Hero hero) {
        hero.setName(heroDto.getName());
        hero.setMaxHp(heroDto.getMaxHp());
        hero.setIntelligence(heroDto.getIntelligence());
        hero.setLuck(heroDto.getLuck());
        hero.setEndurance(heroDto.getEndurance());
        hero.setDexterity(heroDto.getDexterity());
        hero.setDiligence(heroDto.getDiligence());
        hero.setAdventure(heroDto.getAdventure());
        hero.setForint(heroDto.getForint());
        hero.setCrypto(heroDto.getCrypto());
        hero.setXp(heroDto.getXp());
        hero.setMaxXp(heroDto.getMaxXp());
        hero.setLevel(heroDto.getLevel());
        hero.setBoosterItem(heroDto.getBoosterItem() != null ? boosterItemConverter.dtoToBoosterItem(heroDto.getBoosterItem()) : null);
        hero.setBackpackItems(heroDto.getBackpackItems() != null ? heroDto.getBackpackItems().stream().map(itemConverter::dtoToItem).toList() : null);
        hero.setEquipmentItems(heroDto.getEquipmentItems() != null ? heroDto.getEquipmentItems().stream().map(itemConverter::dtoToItem).toList() : null);
        hero.setDungeonLevel(heroDto.getDungeonLevel());
        hero.setNextDungeonTry(heroDto.getNextDungeonTry());
        hero.setImage(heroDto.getImage());
        return hero;
    }

    public HeroDto heroToDto(Hero hero) {
        HeroDto heroDto = new HeroDto();
        heroDto.setName(hero.getName());
        heroDto.setMaxHp(hero.getMaxHp());
        heroDto.setIntelligence(hero.getIntelligence());
        heroDto.setLuck(hero.getLuck());
        heroDto.setEndurance(hero.getEndurance());
        heroDto.setDexterity(hero.getDexterity());
        heroDto.setDiligence(hero.getDiligence());
        heroDto.setAdventure(hero.getAdventure());
        heroDto.setForint(hero.getForint());
        heroDto.setCrypto(hero.getCrypto());
        heroDto.setXp(hero.getXp());
        heroDto.setMaxXp(hero.getMaxXp());
        heroDto.setLevel(hero.getLevel());
        heroDto.setBoosterItem(hero.getBoosterItem() != null ? boosterItemConverter.boosterItemToDto(hero.getBoosterItem()) : null);
        heroDto.setBackpackItems(hero.getBackpackItems() != null ? hero.getBackpackItems().stream().map(itemConverter::itemToDtoWithPics).toList() : null);
        heroDto.setEquipmentItems(hero.getEquipmentItems() != null ? hero.getEquipmentItems().stream().map(itemConverter::itemToDtoWithPics).toList() : null);
        heroDto.setImage(hero.getImage());
        heroDto.setProfileName(hero.getProfile().getUserName());
        heroDto.setDungeonLevel(hero.getDungeonLevel());
        heroDto.setNextDungeonTry(hero.getNextDungeonTry());
        return heroDto;
    }
    public Hero createDtoToHero(HeroDto heroDto, Profile profile) {
        Hero hero = new Hero();
        hero.setName(heroDto.getName());
        hero.setMaxHp(heroDto.getMaxHp());
        hero.setIntelligence(heroDto.getIntelligence());
        hero.setLuck(heroDto.getLuck());
        hero.setEndurance(heroDto.getEndurance());
        hero.setDexterity(heroDto.getDexterity());
        hero.setDiligence(heroDto.getDiligence());
        hero.setAdventure(heroDto.getAdventure());
        hero.setForint(heroDto.getForint());
        hero.setCrypto(heroDto.getCrypto());
        hero.setXp(heroDto.getXp());
        hero.setMaxXp(heroDto.getMaxXp());
        hero.setLevel(heroDto.getLevel());
        heroDto.setBoosterItem(hero.getBoosterItem() != null ? boosterItemConverter.boosterItemToDto(hero.getBoosterItem()) : null);
        hero.setBackpackItems(heroDto.getBackpackItems() != null ? heroDto.getBackpackItems().stream().map(itemConverter::dtoToItem).toList() : null);
        hero.setEquipmentItems(heroDto.getEquipmentItems() != null ? heroDto.getEquipmentItems().stream().map(itemConverter::dtoToItem).toList() : null);
        hero.setDungeonLevel(heroDto.getDungeonLevel());
        hero.setNextDungeonTry(heroDto.getNextDungeonTry());
        hero.setImage(heroDto.getImage());
        hero.setProfile(profile);
        return hero;
    }
}
