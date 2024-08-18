package hu.szte.brawlers.controller;

import hu.szte.brawlers.model.Hero;
import hu.szte.brawlers.model.dto.HeroDto;
import hu.szte.brawlers.model.dto.ItemDto;
import hu.szte.brawlers.service.HeroService;
import hu.szte.brawlers.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;
    private final HeroService heroService;

    @GetMapping("/items")
    private ResponseEntity<List<ItemDto>> listShopItems(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception {
        String[] splittedToken = token.split("Bearer ");
        HeroDto hero = heroService.HeroByProfile(splittedToken[1]);

        return new ResponseEntity<>(shopService.listShopItems(hero.getName()), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    private ResponseEntity<List<ItemDto>> refreshItems(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception {
        String[] splittedToken = token.split("Bearer ");
        HeroDto hero = heroService.HeroByProfile(splittedToken[1]);

        return new ResponseEntity<>(shopService.refreshItems(hero.getName()), HttpStatus.OK);
    }

    @PostMapping("/buyItem")
    private ResponseEntity<HeroDto> buyItem(@RequestParam String itemName) {
        return new ResponseEntity<>(shopService.buyItem(itemName),HttpStatus.OK);
    }

    @PostMapping("/sellItem")
    private ResponseEntity<HeroDto> sellItem(@RequestParam String itemName) {
        return new ResponseEntity<>(shopService.sellItem(itemName),HttpStatus.OK);
    }

}
