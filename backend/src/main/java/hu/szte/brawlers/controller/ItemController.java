package hu.szte.brawlers.controller;

import hu.szte.brawlers.model.dto.ItemDto;
import hu.szte.brawlers.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> findAll() {
        return new ResponseEntity<>(itemService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ItemDto> findById(@PathVariable String name) {
        return new ResponseEntity<>(itemService.getItemByName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemDto> addItem(@RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.addItem(itemDto), HttpStatus.OK);
    }

    @PutMapping("/{name}")
    public ResponseEntity<ItemDto> updateItem(@RequestBody ItemDto itemDto, @PathVariable String name) {
        return new ResponseEntity<>(itemService.updateItem(itemDto, name), HttpStatus.OK);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteItem(@PathVariable String name) {
        itemService.deleteItem(name);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
