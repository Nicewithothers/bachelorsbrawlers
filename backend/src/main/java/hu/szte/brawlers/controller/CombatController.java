package hu.szte.brawlers.controller;

import hu.szte.brawlers.service.FightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
public class CombatController {
    private final FightService fightService;

    @PostMapping("/combat")
    public ResponseEntity<String> simulateCombat(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String monsterName) throws InterruptedException {
        String[] splittedToken = token.split("Bearer ");
        return new ResponseEntity<>(fightService.heroVsMonster(splittedToken[1],monsterName), HttpStatus.OK);
    }
    @PostMapping("/scheduleHeroVsGrunt")
    public String startCounterHeroVsGrunt(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String enemyName) throws InterruptedException {
        String[] splittedToken = token.split("Bearer ");
        return fightService.startCounterheroVsGrunt(splittedToken[1],enemyName);
    }
    @PostMapping("/scheduleHeroVsMonster")
    public String startCounterHeroVsMonster(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String enemyName) throws InterruptedException {
        String[] splittedToken = token.split("Bearer ");
        return fightService.startCounterheroVsMonster(splittedToken[1],enemyName);
    }
    @PostMapping("/scheduleHeroVsHero")
    public String startCounterHeroVsHero(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String enemyName) throws InterruptedException {
        String[] splittedToken = token.split("Bearer ");
        return fightService.startCounterheroVsHeros(splittedToken[1],enemyName);
    }


}
