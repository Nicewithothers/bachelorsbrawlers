package hu.szte.brawlers.controller;

import hu.szte.brawlers.model.dto.BoosterItemDto;
import hu.szte.brawlers.model.dto.HeroDto;
import hu.szte.brawlers.service.TobaccoShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TobaccoShopController {
    private final TobaccoShopService tobaccoShopService;

    @GetMapping("/listBoosterItems")
    public ResponseEntity<List<BoosterItemDto>> listTobaccoShopItems() {
        return new ResponseEntity<>(tobaccoShopService.listBoosterItems(), HttpStatus.OK);
    }

    @PostMapping("/updateBoosterItem")
    public ResponseEntity<HeroDto> buyBoosterItem(@RequestParam String boosterItemName) {
        return new ResponseEntity<>(tobaccoShopService.updateBoosterItem(boosterItemName), HttpStatus.OK);
    }
}
