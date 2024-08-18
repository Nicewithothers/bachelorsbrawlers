package hu.szte.brawlers.controller;

import hu.szte.brawlers.model.GruntName;
import hu.szte.brawlers.model.dto.HeroDto;
import hu.szte.brawlers.model.dto.QuestDto;
import hu.szte.brawlers.service.HeroService;
import hu.szte.brawlers.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tavern")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;
    private final HeroService heroService;

    @GetMapping("/getQuests")
    public ResponseEntity<List<QuestDto>> getMissions(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String[] splittedToken = token.split("Bearer ");
        HeroDto hero = heroService.HeroByProfile(splittedToken[1]);
        return new ResponseEntity<>(questService.getQuests(hero.getName()), HttpStatus.OK);
    }
}
