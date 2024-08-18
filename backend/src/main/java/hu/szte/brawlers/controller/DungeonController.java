package hu.szte.brawlers.controller;

import hu.szte.brawlers.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class DungeonController {
    private final HeroService heroService;


    @GetMapping("/getDungeonLevel")
    public ResponseEntity<Integer> getLevelOfDungeonByHero(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception{
        return new ResponseEntity<>(heroService.dungeonLevelOfHero(token), HttpStatus.OK);
    }
}
