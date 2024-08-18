package hu.szte.brawlers.controller;

import hu.szte.brawlers.model.dto.YearBookDto;
import hu.szte.brawlers.service.YearBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/yearbook")
@RequiredArgsConstructor
public class YearBookController {
    private final YearBookService yearBookService;

    @GetMapping
    public ResponseEntity<List<YearBookDto>> findAll() {
        return new ResponseEntity<>(yearBookService.findAll(), HttpStatus.OK);
    }
}
