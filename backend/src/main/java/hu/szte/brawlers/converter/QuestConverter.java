package hu.szte.brawlers.converter;

import hu.szte.brawlers.model.Quest;
import hu.szte.brawlers.model.dto.QuestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestConverter {
    private final HeroConverter heroConverter;
    private final ItemConverter itemConverter;

    public Quest dtoToQuest(QuestDto questDto) {
        return Quest.builder()
                .cost(questDto.getCost())
                .xp(questDto.getXp())
                .hero(heroConverter.dtoToHero(questDto.getHero()))
                .reward(itemConverter.dtoToItem(questDto.getReward()))
                .monsterName(questDto.getMonsterName())
                .fileName(questDto.getFileName())
                .monsterType(questDto.getMonsterType())
                .build();
    }

    public QuestDto questToDto(Quest quest) {
        return QuestDto.builder()
                .cost(quest.getCost())
                .xp(quest.getXp())
                .hero(heroConverter.heroToDto(quest.getHero()))
                .reward(quest.getReward() != null ? itemConverter.itemToDto(quest.getReward()) : null)
                .monsterName(quest.getMonsterName())
                .fileName(quest.getFileName())
                .monsterType(quest.getMonsterType())
                .build();
    }
}
