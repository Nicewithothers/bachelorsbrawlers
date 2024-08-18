package hu.szte.brawlers.service;

import hu.szte.brawlers.InsufficientFundsException;
import hu.szte.brawlers.converter.BoosterItemConverter;
import hu.szte.brawlers.converter.HeroConverter;
import hu.szte.brawlers.model.BoosterItem;
import hu.szte.brawlers.model.Hero;
import hu.szte.brawlers.model.dto.BoosterItemDto;
import hu.szte.brawlers.model.dto.HeroDto;
import hu.szte.brawlers.repository.HeroRepository;
import hu.szte.brawlers.repository.ProfileRepository;
import hu.szte.brawlers.repository.TobaccoShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TobaccoShopService {
    private final HeroRepository heroRepository;
    private final ProfileRepository profileRepository;
    private final TobaccoShopRepository tobaccoShopRepository;
    private final BoosterItemConverter boosterItemConverter;
    private final HeroConverter heroConverter;
    private final SimpMessagingTemplate messagingTemplate;

    public List<BoosterItemDto> listBoosterItems() {
        List<BoosterItem> items = tobaccoShopRepository.findAll();
        return items.stream().map(boosterItemConverter::boosterItemToDto).toList();
    }

    public HeroDto buyBoosterItem(String boosterItemName) {
        User userPrincipal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BoosterItem boosteritem = tobaccoShopRepository.getBoosterItemByName(boosterItemName).orElseThrow();
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userPrincipal.getUsername()).orElseThrow()).orElseThrow();
        if (hero.getCrypto() >= boosteritem.getPrice()) {
            hero.setCrypto(hero.getCrypto() - boosteritem.getPrice());
            hero.setBoosterItem(boosteritem);
            messagingTemplate.convertAndSend("/topic/crypto-updates", hero.getCrypto());
            heroRepository.save(hero);
        } else {
            throw new InsufficientFundsException("Nincs elég crypto a vásárláshoz!");
        }
        return heroConverter.heroToDto(heroRepository.getHeroByName(hero.getName()).orElseThrow());
    }

    public HeroDto updateBoosterItem(String boosterItemName) {
        User userPrincipal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BoosterItem boosterItem = tobaccoShopRepository.getBoosterItemByName(boosterItemName).orElseThrow();
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userPrincipal.getUsername()).orElseThrow()).orElseThrow();
        if (hero.getBoosterItem() != null) {
            if (hero.getBoosterItem().getEffectiveness() < boosterItem.getEffectiveness()) {
                buyBoosterItem(boosterItem.getName());
            } else {
                throw new RuntimeException("Nem vehetsz gyengébb hatású italt!");
            }
        } else {
            return buyBoosterItem(boosterItem.getName());
        }
        return heroConverter.heroToDto(heroRepository.getHeroByName(hero.getName()).orElseThrow());
    }

    public BoosterItemDto addBoosterItem(BoosterItemDto boosterItemDto) {
        return boosterItemConverter.boosterItemToDto(tobaccoShopRepository.save(boosterItemConverter.dtoToBoosterItem(boosterItemDto)));
    }
}
