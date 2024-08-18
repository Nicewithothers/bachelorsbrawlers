package hu.szte.brawlers.controller;

import hu.szte.brawlers.model.dto.HeroDto;
import hu.szte.brawlers.service.ArenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arena")
@RequiredArgsConstructor
public class ArenaController {
    private final ArenaService arenaService;

    @GetMapping("/opponents")
    public ResponseEntity<List<HeroDto>> findOpponents(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String[] splittedToken = token.split("Bearer ");
        return new ResponseEntity<>(arenaService.findOpponent(splittedToken[1]), HttpStatus.OK);
    }

}
