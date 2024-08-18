package hu.szte.brawlers.service;

import hu.szte.brawlers.model.Grunt;
import hu.szte.brawlers.model.GruntName;
import hu.szte.brawlers.model.Hero;
import org.springframework.stereotype.Service;

@Service
public class GruntService {
    public Grunt generateGrunt(GruntName name, Hero hero) {
        switch (name) {
            case EASY_MONSTER -> {
                return Grunt.builder()
                        .name(name)
                        .maxHp(hero.getMaxHp() / 5)
                        .luck(hero.getLuck() / 3)
                        .dexterity(hero.getDexterity() / 3)
                        .endurance(hero.getEndurance() / 3)
                        .intelligence(hero.getIntelligence() / 5)
                        .build();
            }
            case MEDIUM_MONSTER -> {
                return Grunt.builder()
                        .name(name)
                        .maxHp(hero.getMaxHp() / 3)
                        .luck(hero.getLuck() / 2)
                        .dexterity(hero.getDexterity() / 2)
                        .endurance(hero.getEndurance() / 2)
                        .intelligence(hero.getIntelligence() / 4)
                        .build();

            }
            case HARD_MONSTER -> {
                return Grunt.builder()
                        .name(name)
                        .maxHp(hero.getMaxHp())
                        .luck(hero.getLuck() / 2)
                        .dexterity(hero.getDexterity())
                        .endurance(hero.getEndurance())
                        .intelligence(hero.getIntelligence() / 3)
                        .build();
            }
        }
        return null;
    }
}