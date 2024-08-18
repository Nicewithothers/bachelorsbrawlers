package hu.szte.brawlers.service;

import hu.szte.brawlers.InsufficientFundsException;
import hu.szte.brawlers.converter.HeroConverter;
import hu.szte.brawlers.converter.ItemConverter;
import hu.szte.brawlers.model.Hero;
import hu.szte.brawlers.model.Item;
import hu.szte.brawlers.model.dto.HeroDto;
import hu.szte.brawlers.model.dto.ItemDto;
import hu.szte.brawlers.repository.HeroRepository;
import hu.szte.brawlers.repository.ItemRepository;
import hu.szte.brawlers.repository.ProfileRepository;

import jakarta.transaction.Transactional;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import hu.szte.brawlers.service.MinioService;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final HeroRepository heroRepository;
    private final ProfileRepository profileRepository;
    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;
    private final HeroConverter heroConverter;
    private final SimpMessagingTemplate messagingTemplate;

    

    @Cacheable(cacheNames = "shopItems", key = "#heroName")
    public List<ItemDto> listShopItems(String heroName){
        return generateShopItems(heroName);
    }

    @CacheEvict(cacheNames = "shopItems", key = "#heroName")
    // @Cacheable(cacheNames = "shopItems", key = "#heroName")
    public List<ItemDto> refreshItems(String heroName) {
        User userPrincipal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userPrincipal.getUsername()).orElseThrow()).orElseThrow();
        if (hero.getCrypto() < 2) {
            throw new InsufficientFundsException("Nincs elég crypto a frissítéshez!");
        }
        hero.setCrypto(hero.getCrypto() - 2);
        messagingTemplate.convertAndSend("/topic/crypto-updates", hero.getCrypto());
        heroRepository.save(hero);
        
        return listShopItems(heroName);
    }

    // @Cacheable(cacheNames = "shopItems", key = "#heroName")
    public List<ItemDto> generateShopItems(String heroName) {
        User userPrincipal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userPrincipal.getUsername()).orElseThrow()).orElseThrow();
        // Hero hero = heroRepository.getHeroByName(heroName).orElseThrow();

        List<Item> heroItems = new ArrayList<>();
        heroItems.addAll(hero.getBackpackItems());
        heroItems.addAll(hero.getEquipmentItems());

        List<Item> newItems = itemRepository.findAll();
        newItems.removeAll(heroItems);
        Collections.shuffle(newItems);
        return newItems.subList(0,6).stream().map(itemConverter::itemToDtoWithPics).toList();
    }


    public HeroDto buyItem(String itemName){
        User userPrincipal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Item item = itemRepository.getItemByName(itemName).orElseThrow();
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userPrincipal.getUsername()).orElseThrow()).orElseThrow();
        if((hero.getBackpackItems() != null ? hero.getBackpackItems().size() : 0) < 10){
            if(hero.getForint()>=item.getPrice()){
                hero.setForint(hero.getForint()-item.getPrice());
                hero.getBackpackItems().add(item);
                heroRepository.save(hero);
                messagingTemplate.convertAndSend("/topic/forint-updates", hero.getForint());
            } else {
                throw new InsufficientFundsException("Nincs elég pénzed a tárgy megvásárlásához!");
            }
        }
              
        return heroConverter.heroToDto(heroRepository.getHeroByName(hero.getName()).orElseThrow());
    }
    
    public HeroDto sellItem(String itemName){
        User userPrincipal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Item item = itemRepository.getItemByName(itemName).orElseThrow();
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userPrincipal.getUsername()).orElseThrow()).orElseThrow();
        if(hero.getBackpackItems().contains(item)){
                hero.setForint(hero.getForint()+(item.getPrice()-1));
                hero.getBackpackItems().remove(item);
                heroRepository.save(hero);
                messagingTemplate.convertAndSend("/topic/forint-updates", hero.getForint());
        }
        return heroConverter.heroToDto(heroRepository.getHeroByName(hero.getName()).orElseThrow());
    }
}
