package hu.szte.brawlers.service;

import hu.szte.brawlers.converter.QuestConverter;
import hu.szte.brawlers.model.*;
import hu.szte.brawlers.model.dto.QuestDto;
import hu.szte.brawlers.repository.HeroRepository;
import hu.szte.brawlers.repository.QuestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuestService {
    private final HeroRepository heroRepository;
    private final QuestRepository questRepository;
    private final QuestConverter questConverter;
    private final ItemService itemService;
    private final MinioService minioService;

    @Transactional
    public List<QuestDto> getQuests(String heroName) {
        List<QuestDto> questList = questRepository.findAllByHeroName(heroName).stream().map(quest -> {
            QuestDto questDto = questConverter.questToDto(quest);
            byte[] fileData = minioService.downloadFile("bachelorsbrawlers", "mob/" + quest.getFileName() + ".png");
            questDto.setImage(fileData);
            return questDto;
        }).toList();

        if(!questList.isEmpty()) {
            return questList;
        }

        generateQuests(heroName);

        return questRepository.findAllByHeroName(heroName).stream().map(questConverter::questToDto).toList();
    }

    public Map.Entry<String, String> setRandomGruntImage() {
        TreeMap<String, String> gruntImagesWithNames = new TreeMap<>() {{
            put("bmw_sofor", "BMW-s sofőr");
            put("drogos_hajlektalan", "Drogos hajléktalan");
            put("ellenor", "Ellenőr");
            put("gordeszkas_hajlektalan", "Gördeszkás hajléktalan");
            put("gorkoris_hajlektalan", "Görkoris hajléktalan");
            put("gyermekvedelmi", "Gyermekvédelmis");
            put("hajlektalan", "Hajléktalan");
            put("jates_kidobo", "Jate-s kidobó");
            put("jblesek", "JBLesek");
            put("jehova_tanui", "Jehova tanúi");
            put("kirsnas_terito", "Kirsnas térítő");
            put("kutyashajlektalan", "Kutyás hajléktalan");
            put("marsteri_atkozodo_jos", "Mars téri átkozódó jós");
            put("meno_gyerek", "Menő gyerek");
            put("ordibalo_oregasszony_a_volan_mogott", "Ordibáló öregasszony a volán mögött");
            put("ordibalo_oregember_a_volan_mogott", "Ordibáló öregember a volán mögött");
            put("oreg_turista_segwayen", "Öreg túrista segway-en");
            put("oregasszony_a_volan_mogott", "Öregasszony a volán mögött");
            put("oregember_a_volan_mogott", "Öregember a volán mögött");
            put("reszeg", "Részeg");
            put("reszegno", "Részeg nő");
            put("rolleres", "Rolleres");
            put("szorolapos_neni", "Szórólapos néni");
            put("utcai_bibliaarus", "Utcai bibliaárus");
            put("vak_taxisofor", "Vak taxisofőr");
        }};

        List<Map.Entry<String, String>> entries = new ArrayList<>(gruntImagesWithNames.entrySet());
        Collections.shuffle(entries);
        Map<String, String> newGruntImagesWithNames = new LinkedHashMap<>();
        for (Map.Entry<String, String> e: entries) {
            newGruntImagesWithNames.put(e.getKey(), e.getValue());
        }
        return newGruntImagesWithNames.entrySet().iterator().next();
    }

    @Transactional
    public void generateQuests(String heroName) {
        questRepository.deleteAllByHeroName(heroName);
        Hero hero = heroRepository.getHeroByName(heroName).orElseThrow();

        questRepository.save(createQuest(GruntName.EASY_MONSTER, hero));
        questRepository.save(createQuest(GruntName.MEDIUM_MONSTER, hero));
        questRepository.save(createQuest(GruntName.HARD_MONSTER, hero));
    }

    private Quest createQuest(GruntName monsterType, Hero hero) {
        int cost;
        long xp;
        Item reward;
        Map.Entry<String, String> grunt = setRandomGruntImage();

        switch (monsterType) {
            case EASY_MONSTER -> {
                cost = calculatePossibleWinnings(0.8, hero);
                xp = calculatePossibleWinnings(0.8, hero);
                reward = itemService.getItemByRarityList(
                        new Rarity[] {Rarity.COMMON, Rarity.RARE, Rarity.UNIQUE},
                        new Integer[] {75, 15, 10}
                );
            }
            case MEDIUM_MONSTER -> {
                cost = calculatePossibleWinnings(1, hero);
                xp = calculatePossibleWinnings(1, hero);
                reward = itemService.getItemByRarityList(
                        new Rarity[] {Rarity.COMMON, Rarity.RARE, Rarity.UNIQUE},
                        new Integer[] {30, 50, 20}
                );
            }
            case HARD_MONSTER -> {
                cost = calculatePossibleWinnings(1.2, hero);
                xp = calculatePossibleWinnings(1.2, hero);
                reward = itemService.getItemByRarityList(
                        new Rarity[] {Rarity.COMMON, Rarity.RARE, Rarity.UNIQUE},
                        new Integer[] {10, 30, 60}
                );
            }
            default -> throw new EnumConstantNotPresentException(GruntName.class, monsterType.name());
        }

        //csak kb 30% esellyel dobhat itemet a mob
        if((new Random().nextInt(100) > 30)) {
            reward = null;
        }

        return Quest
                .builder()
                .monsterName(grunt.getValue())
                .hero(hero)
                .cost(cost)
                .reward(reward)
                .xp(xp)
                .fileName(grunt.getKey())
                .monsterType(monsterType)
                .build();
    }

    private int calculatePossibleWinnings(double multiplier, Hero hero) {
        return (int) (multiplier * (hero.getAdventure() / 8));
    }

    @Transactional
    public Quest chooseQuest(GruntName gruntName, String heroName) {
        Hero hero = heroRepository.getHeroByName(heroName).orElseThrow();
        Quest quest = questRepository.getByMonsterTypeAndHero(gruntName, hero).orElseThrow();

        int adventure = (hero.getBoosterItem() == null)
            ? hero.getAdventure() - quest.getCost()
            : (int) (hero.getAdventure() - quest.getCost() * (1 - hero.getBoosterItem().getEffectiveness()));

        hero.setAdventure(adventure);
        heroRepository.save(hero);
        generateQuests(heroName);
        return quest;
    }
}
