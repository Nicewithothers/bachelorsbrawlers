package hu.szte.brawlers.controller;

import hu.szte.brawlers.InsufficientFundsException;
import hu.szte.brawlers.model.dto.GamblingDto;
import hu.szte.brawlers.model.dto.HeroDto;
import hu.szte.brawlers.service.HeroService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/heroes")
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;

    @GetMapping
    public ResponseEntity<List<HeroDto>> findAll() {
        return new ResponseEntity<>(heroService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<HeroDto>> findByXpDescending() {
        return new ResponseEntity<>(heroService.findByXpDescending(), HttpStatus.OK);
    }

    @GetMapping("/getHero")
    public ResponseEntity<HeroDto> HeroByProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception {
        String[] splittedToken = token.split("Bearer ");
        return new ResponseEntity<>(heroService.HeroByProfile(splittedToken[1]), HttpStatus.OK);
    }

    @PostMapping("/createHero")
    public ResponseEntity<HeroDto> addHero(@RequestBody HeroDto heroDto) {
        return new ResponseEntity<>(heroService.addHero(heroDto), HttpStatus.OK);
    }

    //@TODO https://git-okt.sed.inf.szte.hu/ib670g_24_agilisszoftverfejlesztes/ib670g-5_fea/bachelors_brawlers/-/merge_requests/65 alapján tokenre cserélni a namet
    @PutMapping("/{heroName}")
    public ResponseEntity<HeroDto> updateHero(@RequestBody HeroDto heroDto, @PathVariable String heroName) {
        return new ResponseEntity<>(heroService.updateHero(heroDto, heroName), HttpStatus.OK);
    }

    //@TODO https://git-okt.sed.inf.szte.hu/ib670g_24_agilisszoftverfejlesztes/ib670g-5_fea/bachelors_brawlers/-/merge_requests/65 alapján tokenre cserélni a namet
    @DeleteMapping("/{heroName}")
    public ResponseEntity<String> deleteHero(@PathVariable String heroName) {
        heroService.deleteHero(heroName);
        return new ResponseEntity<>(heroService.deleteHero(heroName) ? "Deleted":"Not Deleted", HttpStatus.OK);
    }

    @PostMapping("/equip")
    public ResponseEntity<HeroDto> equipItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String itemName) throws Exception {
        String[] splittedToken = token.split("Bearer ");
        return new ResponseEntity<>(heroService.equipItem(splittedToken[1], itemName), HttpStatus.OK);
    }

    @PostMapping("/unequip")
    public ResponseEntity<HeroDto> unequipItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String itemName) throws Exception {
        String[] splittedToken = token.split("Bearer ");
        return new ResponseEntity<>(heroService.unequipItem(splittedToken[1], itemName), HttpStatus.OK);
    }

    @PostMapping("/{statName}/increase")
    public ResponseEntity<?> increaseHeroStat(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String statName)throws Exception {
        try {
            String[] splittedToken = token.split("Bearer ");
            return new ResponseEntity<>(heroService.increaseHeroStat(splittedToken[1], statName), HttpStatus.OK);
        } catch (InsufficientFundsException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    @GetMapping("/{statName}/nextCost")
    public ResponseEntity<Integer> getNextStatIncreaseCost(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String statName) {
        String[] splittedToken = token.split("Bearer ");
        int nextLevel = heroService.getHeroStatLevel(splittedToken[1], statName) + 1;
        int nextCost = heroService.calculateFibonacci(nextLevel);
        return new ResponseEntity<>(nextCost, HttpStatus.OK);
    }

    @PostMapping("/startGambling")
    public ResponseEntity<GamblingDto> startGambling(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String[] splittedToken = token.split("Bearer ");
        return ResponseEntity.ok(heroService.gambling(splittedToken[1]));
    }

    @PostMapping("/claimVictory")
    public ResponseEntity<Integer> claimGamblingVictory(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String[] splittedToken = token.split("Bearer ");
        return ResponseEntity.ok(heroService.claimVictory(splittedToken[1]));
    }


    @GetMapping("/lastWin")
    public ResponseEntity<Integer> getLastWin(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String[] splittedToken = token.split("Bearer ");
        return ResponseEntity.ok(heroService.getLastWin(splittedToken[1]));
    }

    @GetMapping("/dungeonLevel")
    public ResponseEntity<Integer> getDungeonLevel(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String[] splittedToken = token.split("Bearer ");
        return ResponseEntity.ok(heroService.getDungeonLevel(splittedToken[1]));
    }
    @GetMapping("/dungeonNextTryTime")
    public ResponseEntity<LocalDateTime> getNextTryTime(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String[] splittedToken = token.split("Bearer ");
        return ResponseEntity.ok(heroService.getNextTryTime(splittedToken[1]));
    }

    @GetMapping("/getHeroesByIds")
    public ResponseEntity<List<HeroDto>> getHeroesByIds(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                        @RequestParam("heroNames") List<String> names
                                                       ) throws Exception {
        String[] splittedToken = token.split("Bearer ");
        return new ResponseEntity<>(heroService.getHerosByIds(splittedToken[1], names), HttpStatus.OK);
    }


}
