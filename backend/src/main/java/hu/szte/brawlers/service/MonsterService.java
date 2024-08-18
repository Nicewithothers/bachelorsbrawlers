package hu.szte.brawlers.service;

import hu.szte.brawlers.converter.MonsterConverter;
import hu.szte.brawlers.model.Monster;

import hu.szte.brawlers.model.dto.MonsterDto;
import hu.szte.brawlers.repository.MonsterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonsterService {
    private final MonsterRepository monsterRepository;
    private final MonsterConverter monsterConverter;
    private final MinioService minioService;

    @Cacheable(value = "monsterCache")
    public List<MonsterDto> findAll() {
        return monsterRepository.findAll().stream().map(monster -> {
            MonsterDto monsterDto = monsterConverter.monsterToDto(monster);
            byte[] fileData = minioService.downloadFile("bachelorsbrawlers", "boss/" + monster.getImage() + ".webp");
            monsterDto.setPicture(fileData);
            return monsterDto;
        }).toList();
    }

    public MonsterDto addMonster(MonsterDto monsterDto) {
        return monsterConverter.monsterToDto(monsterRepository.save(monsterConverter.dtoToMonster(monsterDto)));
    }

    public MonsterDto updateMonster(MonsterDto monsterDto, String name) {
        Monster monster = monsterRepository.getMonsterByName(name).orElseThrow();
        return monsterConverter.monsterToDto(monsterRepository.save(monsterConverter.updateMonster(monsterDto, monster)));
    }

    public void deleteMonster(String name) {
        monsterRepository.deleteByName(name);
    }

    public Integer updateMonsterEndurance(String name, Integer change) {
        Monster monster = monsterRepository.getMonsterByName(name).orElseThrow();
        monster.setEndurance(monster.getEndurance() + change);
        monsterRepository.save(monster);
        return monster.getEndurance();
    }
}
