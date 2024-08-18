package hu.szte.brawlers.controller;

import hu.szte.brawlers.model.dto.HeroDto;
import hu.szte.brawlers.model.dto.MailDto;
import hu.szte.brawlers.service.HeroService;
import hu.szte.brawlers.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mailing")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    private final HeroService heroService;

    @GetMapping("/mails")
    public ResponseEntity<List<MailDto>> getMails(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String[] splittedToken = token.split("Bearer ");
        HeroDto hero = heroService.HeroByProfile(splittedToken[1]);

        return new ResponseEntity<>(mailService.getMails(hero.getName()), HttpStatus.OK);
    }

    @GetMapping("/unread")
    public ResponseEntity<Integer> getUnread(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String[] splittedToken = token.split("Bearer ");
        HeroDto hero = heroService.HeroByProfile(splittedToken[1]);

        return new ResponseEntity<>(mailService.getUnread(hero.getName()), HttpStatus.OK);
    }
}
