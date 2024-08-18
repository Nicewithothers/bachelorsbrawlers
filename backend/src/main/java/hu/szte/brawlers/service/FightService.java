package hu.szte.brawlers.service;

import hu.szte.brawlers.converter.ItemConverter;
import hu.szte.brawlers.converter.MonsterConverter;
import hu.szte.brawlers.model.*;
import hu.szte.brawlers.model.dto.AttackDto;
import hu.szte.brawlers.model.dto.MonsterDto;
import hu.szte.brawlers.model.dto.YearBookDto;
import hu.szte.brawlers.repository.HeroRepository;
import hu.szte.brawlers.repository.MonsterRepository;
import hu.szte.brawlers.repository.ProfileRepository;
import hu.szte.brawlers.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class FightService {
    private final JwtUtil jwtUtil;
    private final ProfileRepository profileRepository;
    private final HeroRepository heroRepository;
    private final MonsterRepository monsterRepository;
    private final CombatUtilService combatUtilService;
    private final SimpMessagingTemplate messagingTemplate;
    private final GruntService gruntService;
    private final MailService mailService;
    private final QuestService questService;
    private final HeroService heroService;
    private final ItemConverter itemConverter;
    private final MonsterConverter monsterConverter;
    private final YearBookService yearBookService;


    private int counter = 0;

    public String startCounterheroVsGrunt(String token, String gruntName) throws InterruptedException {
        counter = 6;
        return heroVsGrunt(token, gruntName);
    }
    public String startCounterheroVsMonster(String token, String monsterName) throws InterruptedException {
        counter = 6;
        return heroVsMonster(token, monsterName);
    }
    public String startCounterheroVsHeros(String token, String enemyHero) throws InterruptedException {
        counter = 6;
        return heroVsHero(token, enemyHero);
    }

    private void sendCounterValueToFrontend(AttackDto attackDto) {
        messagingTemplate.convertAndSend("/topic/schedule", attackDto);
    }

    public boolean gruntFight() {
        Random winRate = new Random();
        int winOrLose = winRate.nextInt(100);
        return winOrLose < 90;
    }

    public String heroVsGrunt(String token, String gruntName) throws InterruptedException {
        String userName = jwtUtil.extractUsername(token);
        profileRepository.findByUserName(userName).orElseThrow();
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();

        Quest quest = questService.chooseQuest(GruntName.valueOf(gruntName), hero.getName());
        Grunt grunt = gruntService.generateGrunt(GruntName.valueOf(gruntName), hero);
        
        Integer heroHP = hero.getMaxHp();
        Integer gruntHP = grunt.getMaxHp();
        while ((heroHP != 0 && gruntHP != 0) && counter > 0) {
            Damage damage;
            AttackDto attackDto;
            counter--;
            if (counter % 2 == 0) {
                damage = combatUtilService.calculateDamage(hero.getIntelligence(), hero.getLuck(), grunt.getDexterity(), hero.getEquipmentItems());
                gruntHP -= damage.getValue();
                attackDto = new AttackDto(damage, true);
            } else {
                damage = combatUtilService.calculateDamage(grunt.getIntelligence(), grunt.getLuck(), hero.getDexterity(), null);
                heroHP -= damage.getValue();
                attackDto = new AttackDto(damage, false);
            }
            sendCounterValueToFrontend(attackDto);
            Thread.sleep(2000); // Aluggyá
        }
        // Befejezzuk gyorsan a fightot, ezt a frontend mar nem kapja meg, csak az eredmenyt
        int i = 0;
        while (heroHP > 0 && gruntHP > 0) {
            Damage damage;
            if (i % 2 == 0) {
                damage = combatUtilService.calculateDamage(hero.getIntelligence(), hero.getLuck(), grunt.getDexterity(), hero.getEquipmentItems());
                gruntHP -= damage.getValue();
            } else {
                damage = combatUtilService.calculateDamage(grunt.getIntelligence(), grunt.getLuck(), hero.getDexterity(), null);
                heroHP -= damage.getValue();
            }
            i++;
        }
        if (heroHP > 0) {
            if (quest.getReward() != null) {
                heroService.lootItem(token, itemConverter.itemToDto(quest.getReward()));
            }
            heroService.handleLevelUp(hero.getName(), quest.getXp()*hero.getLevel());
            heroRepository.save(hero);
            return "Hero win";
        } else {
            return "Grunt win";
        }
    }
    public String heroVsMonster(String token, String monsterName) throws InterruptedException {
        String userName = jwtUtil.extractUsername(token);
        profileRepository.findByUserName(userName).orElseThrow();
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();
        if(hero.getNextDungeonTry().isAfter(LocalDateTime.now())){throw new UnsupportedOperationException();}
        Monster monster = monsterRepository.getMonsterByName(monsterName).orElseThrow();
        Integer heroHP = hero.getMaxHp();
        Integer monsterHP = monster.getMaxHp();
        MonsterDto finalLvlMonster = monsterConverter.monsterToDto(monsterRepository.findTopByOrderByDungeonLevelDesc().get());
        while ((heroHP != 0 && monsterHP != 0) && counter > 0) {
            Damage damage;
            AttackDto attackDto;
            counter--;
            if (counter % 2 == 0) {
                damage = combatUtilService.calculateDamage(hero.getIntelligence(), hero.getLuck(), monster.getDexterity(), hero.getEquipmentItems());
                monsterHP -= damage.getValue();
                attackDto = new AttackDto(damage, true);
            } else {
                damage = combatUtilService.calculateDamage(monster.getIntelligence(), monster.getLuck(), hero.getDexterity(),null);
                heroHP -= damage.getValue();
                attackDto = new AttackDto(damage, false);
            }
            sendCounterValueToFrontend(attackDto);
            Thread.sleep(2000);
        }
        int i = 0;
        while (heroHP > 0 && monsterHP > 0) {
            Damage damage;
            if (i % 2 == 0) {
                damage = combatUtilService.calculateDamage(hero.getIntelligence(), hero.getLuck(), monster.getDexterity(), hero.getEquipmentItems());
                monsterHP -= damage.getValue();
            } else {
                damage = combatUtilService.calculateDamage(monster.getIntelligence(), monster.getLuck(), hero.getDexterity(), null);
                heroHP -= damage.getValue();
            }
            i++;
        }
        if (heroHP > 0) {
            mailService.addMail(hero.getName(),monster.getName(),true,hero.getDungeonLevel()*500, "Ösztöndíj");
            hero.setDungeonLevel(hero.getDungeonLevel()+1);
            hero.setNextDungeonTry(LocalDateTime.now().plusMinutes(5));
            getMonsterReward(hero);
            heroRepository.save(hero);
            if (finalLvlMonster.getDungeonLevel() == monster.getDungeonLevel()){
                YearBookDto yearBook = YearBookDto.builder()
                        .heroId(hero.getId())
                        .heroLevel(hero.getLevel())
                        .defeatDateTime(LocalDateTime.now())
                        .build();
                yearBookService.addYearBook(yearBook);
            }
            return "Hero win";
        } else {
            hero.setNextDungeonTry(LocalDateTime.now().plusMinutes(5));
            heroRepository.save(hero);
            return "Monster win";
        }
    }
    public String heroVsHero(String token, String heroName) throws InterruptedException {
        String userName = jwtUtil.extractUsername(token);
        profileRepository.findByUserName(userName).orElseThrow();
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();
        Hero enemyHero = heroRepository.getHeroByName(heroName).orElseThrow();
        Integer heroHP = hero.getMaxHp();
        Integer enemyHeroMaxHp = enemyHero.getMaxHp();
        while ((heroHP != 0 && enemyHeroMaxHp != 0) && counter > 0) {
            Damage damage;
            AttackDto attackDto;
            counter--;
            if (counter % 2 == 0) {
                damage = combatUtilService.calculateDamage(hero.getIntelligence(), hero.getLuck(), enemyHero.getDexterity(), hero.getEquipmentItems());
                enemyHeroMaxHp -= damage.getValue();
                attackDto = new AttackDto(damage, true);
            } else {
                damage = combatUtilService.calculateDamage(enemyHero.getIntelligence(), enemyHero.getLuck(), hero.getDexterity(), enemyHero.getEquipmentItems());
                heroHP -= damage.getValue();
                attackDto = new AttackDto(damage, false);
            }
            sendCounterValueToFrontend(attackDto);
            Thread.sleep(2000);
        }
        int i = 0;
        while (heroHP > 0 && enemyHeroMaxHp > 0) {
            Damage damage;
            if (i % 2 == 0) {
                damage = combatUtilService.calculateDamage(hero.getIntelligence(), hero.getLuck(), enemyHero.getDexterity(), hero.getEquipmentItems());
                enemyHeroMaxHp -= damage.getValue();
            } else {
                damage = combatUtilService.calculateDamage(enemyHero.getIntelligence(), enemyHero.getLuck(), hero.getDexterity(), enemyHero.getEquipmentItems());
                heroHP -= damage.getValue();
            }
            i++;
        }
        if (heroHP > enemyHeroMaxHp) {
            mailService.addMail(enemyHero.getName(),hero.getName(), false, (int)(enemyHero.getForint()*0.1), "Harc");
            hero.setForint(hero.getForint() + (int) (enemyHero.getForint()*0.1));
            enemyHero.setForint((int) (enemyHero.getForint()*0.9));
            messagingTemplate.convertAndSend("/topic/forint-updates", hero.getForint());
            heroService.handleLevelUp(hero.getName(), Integer.toUnsignedLong(Math.round((float) enemyHero.getXp() /2)));
            heroRepository.save(hero);
            heroRepository.save(enemyHero);
            return "Hero win";
        } else {
            mailService.addMail(enemyHero.getName(),hero.getName(), true, (int)(hero.getForint()*0.1), "Harc");
            enemyHero.setForint(enemyHero.getForint() + (int) (hero.getForint()*0.1));
            hero.setForint((int) (hero.getForint()*0.9));
            messagingTemplate.convertAndSend("/topic/forint-updates", hero.getForint());
            heroRepository.save(hero);
            heroRepository.save(enemyHero);
            return "Enemy win";
        }
    }

    private void getMonsterReward(Hero hero){
        Integer dungeonLevel = hero.getDungeonLevel();
        Integer forintReward = hero.getDungeonLevel()*500;
        Integer cryptoReward = (dungeonLevel/5)+1;
        hero.setForint(hero.getForint()+forintReward);
        hero.setCrypto(hero.getCrypto()+cryptoReward);
        heroService.handleLevelUp(hero.getName(), Integer.toUnsignedLong(dungeonLevel*600));
    }
}
