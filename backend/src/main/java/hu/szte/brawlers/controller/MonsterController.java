package hu.szte.brawlers.controller;

import hu.szte.brawlers.model.dto.MonsterDto;
import hu.szte.brawlers.service.MonsterService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monsters")
@RequiredArgsConstructor
public class MonsterController {
    private final MonsterService monsterService;


    @GetMapping()
    public ResponseEntity<List<MonsterDto>> findAll() {
        return new ResponseEntity<>(monsterService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MonsterDto> addHero(@RequestBody MonsterDto monsterDto) {
        return new ResponseEntity<>(monsterService.addMonster(monsterDto), HttpStatus.OK);
    }

    @PutMapping("/{name}")
    public ResponseEntity<MonsterDto> updateHero(@RequestBody MonsterDto monsterDto, @PathVariable String name) {
        return new ResponseEntity<>(monsterService.updateMonster(monsterDto, name), HttpStatus.OK);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deletePlayer(@PathVariable String name) {
        monsterService.deleteMonster(name);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
