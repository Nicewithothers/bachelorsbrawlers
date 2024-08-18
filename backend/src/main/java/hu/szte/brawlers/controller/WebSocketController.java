package hu.szte.brawlers.controller;


import hu.szte.brawlers.model.UpdateMessage;
import hu.szte.brawlers.service.FightService;
import hu.szte.brawlers.service.HeroService;
import hu.szte.brawlers.service.MonsterService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@AllArgsConstructor
public class WebSocketController {
    private HeroService heroService;
    private MonsterService monsterService;
    private FightService fightService;

    @MessageMapping("/updateHeroEndurance")
    @SendTo("/topic/heroEnduranceUpdates")
    public UpdateMessage handleHeroEnduranceUpdate(UpdateMessage message) {
        message.setChange(heroService.updateHeroEndurance(message.getCharacterName(), message.getChange()));
        return message;
    }

    @MessageMapping("/updateMonsterEndurance")
    @SendTo("/topic/monsterEnduranceUpdates")
    public UpdateMessage handleMonsterEnduranceUpdate(UpdateMessage message) {
        message.setChange(monsterService.updateMonsterEndurance(message.getCharacterName(), message.getChange()));
        return message;
    }


    @MessageMapping("/updateForint")
    @SendTo("/topic/forintUpdates")
    public UpdateMessage handleForintUpdate(UpdateMessage message) {
        message.setChange(heroService.updateHeroForint(message.getCharacterName(), message.getChange()));
        return message;
    }

    @MessageMapping("/updateCrypto")
    @SendTo("/topic/CryptoUpdates")
    public UpdateMessage handleCryptoUpdate(UpdateMessage message) {
        message.setChange(heroService.updateHeroCrypto(message.getCharacterName(), message.getChange()));
        return message;
    }
    @MessageMapping("/schedule")
    @SendTo("/topic/schedule")
    public UpdateMessage startCounter(UpdateMessage message) {
//        message.setChange();
        return message;
    }

}
