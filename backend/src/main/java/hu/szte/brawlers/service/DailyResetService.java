package hu.szte.brawlers.service;

import hu.szte.brawlers.model.Hero;
import hu.szte.brawlers.repository.HeroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyResetService {

    private final HeroRepository heroRepository;

    private final QuestService questService;

    private final ShopService shopService;

    private final CacheManager cacheManager;

    private final int MAX_ADVENTURE = 100;

    private final int RESET_TIME_MINUTES = 20;


    //Ha kell tobb cache is rakotheto
    public void resetShop(Hero hero) {
        cacheManager.getCache("shopItems").clear();
        shopService.generateShopItems(hero.getName());
    }

    private void resetAdventure(Hero hero) {
        hero.setAdventure(MAX_ADVENTURE);
        heroRepository.save(hero);
    }

    private void resetTavern(Hero hero) {
        questService.generateQuests(hero.getName());
    }

    /**
     * Resets shop, adventure and tavern every 20 minutes
     */

    @Scheduled(fixedRate = RESET_TIME_MINUTES * 60 * 1000)
    public void doReset() {
        for(Hero hero: heroRepository.findAll()){
            resetAdventure(hero);
            resetShop(hero);
            resetTavern(hero);
        }
    }
}
