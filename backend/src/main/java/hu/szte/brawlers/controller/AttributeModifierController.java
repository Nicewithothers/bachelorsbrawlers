package hu.szte.brawlers.controller;

import hu.szte.brawlers.model.dto.AttributeModifierDto;
import hu.szte.brawlers.service.AttributeModifierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/modifier")
@RequiredArgsConstructor
public class AttributeModifierController {
    private final AttributeModifierService attributeModifierService;

    @GetMapping
    public ResponseEntity<List<AttributeModifierDto>> findAll() {
        return new ResponseEntity<>(attributeModifierService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttributeModifierDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(attributeModifierService.getAttributeModifierById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AttributeModifierDto> addAttributeModifier(@RequestBody AttributeModifierDto attributeModifierDto) {
        return new ResponseEntity<>(attributeModifierService.addAttributeModifier(attributeModifierDto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttributeModifierDto> updateAttributeModifier(@RequestBody AttributeModifierDto attributeModifierDto, @PathVariable Long id) {
        return new ResponseEntity<>(attributeModifierService.updateAttributeModifier(attributeModifierDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttributeModifier(@PathVariable Long id) {
        attributeModifierService.deleteAttributeModifier(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
