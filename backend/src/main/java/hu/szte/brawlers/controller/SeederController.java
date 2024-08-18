package hu.szte.brawlers.controller;

import hu.szte.brawlers.service.SeederService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seeder")
@RequiredArgsConstructor
public class SeederController {

    private final SeederService seederService;
    
    @PostMapping
    public ResponseEntity<String> runSeeder() {
        seederService.runAttributeModifierSeed();
        seederService.runMonsterSeeder();
        seederService.runItemSeeder();
        seederService.runBoosterItemSeeder();

        return new ResponseEntity<>("seed", HttpStatus.OK);
    }

    // @PostMapping("/delete")
    // public ResponseEntity<String> runDeleteAll() {
    //     seederService.deleteAllItems();
    //     seederService.deleteAllAttributeModifiers();
    //     seederService.deleteAllMonsters();
    //     seederService.deleteAllBoosterItems();

    //     return new ResponseEntity<>("delete", HttpStatus.OK);
    // }
}
