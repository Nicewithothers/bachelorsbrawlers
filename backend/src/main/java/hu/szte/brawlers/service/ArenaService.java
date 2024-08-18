package hu.szte.brawlers.service;

import hu.szte.brawlers.converter.HeroConverter;
import hu.szte.brawlers.model.Hero;
import hu.szte.brawlers.model.Profile;
import hu.szte.brawlers.model.dto.HeroDto;
import hu.szte.brawlers.repository.HeroRepository;
import hu.szte.brawlers.repository.ProfileRepository;
import hu.szte.brawlers.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArenaService {

    private final JwtUtil jwtUtil;
    private final ProfileRepository profileRepository;
    private final HeroRepository heroRepository;
    private final HeroConverter heroConverter;
    private final MinioService minioService;

    public List<HeroDto> findOpponent(String token) {
        String userName = jwtUtil.extractUsername(token);
        Profile profile = profileRepository.findByUserName(userName).orElseThrow();
        Hero hero = heroRepository.getHeroByProfile(profile).orElseThrow();
        List<HeroDto> heroesList = heroRepository.findAll().stream()
                .filter(h -> !h.getProfile().getUserName().equals(profile.getUserName()))
                .map(h -> {
            HeroDto heroDto = heroConverter.heroToDto(h);
            byte[] fileData = minioService.downloadFile("bachelorsbrawlers", h.getProfile().getUserName() + ".png");
            heroDto.setPicture(fileData);
            return heroDto;
        }).toList();
        Long xp = hero.getXp();

        return findClosestHeroesByXp(heroesList, xp);
    }
    private List<HeroDto> findClosestHeroesByXp(List<HeroDto> heroList, Long xp){
        Map<HeroDto, Long> xpDiffs = new HashMap<>();

        for (HeroDto hero: heroList){
            Long difference = Math.abs(hero.getXp() - xp);
            xpDiffs.put(hero, difference);
        }

        return xpDiffs.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .limit(3)
                .toList();
    }
}
