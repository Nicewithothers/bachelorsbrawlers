package hu.szte.brawlers.converter;

import hu.szte.brawlers.model.BoosterItem;
import hu.szte.brawlers.model.dto.BoosterItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoosterItemConverter {
    public BoosterItemDto boosterItemToDto(BoosterItem boosterItem) {
        return BoosterItemDto.builder()
                .name(boosterItem.getName())
                .description(boosterItem.getDescription())
                .effectiveness(boosterItem.getEffectiveness())
                .price(boosterItem.getPrice())
                .build();
    }

    public BoosterItem dtoToBoosterItem(BoosterItemDto boosterItemDto) {
        return BoosterItem.builder()
                .name(boosterItemDto.getName())
                .description(boosterItemDto.getDescription())
                .effectiveness(boosterItemDto.getEffectiveness())
                .price(boosterItemDto.getPrice())
                .build();
    }
}
