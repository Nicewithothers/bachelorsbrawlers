package hu.szte.brawlers.service;

import hu.szte.brawlers.converter.ItemConverter;
import hu.szte.brawlers.model.AttributeModifier;
import hu.szte.brawlers.model.Item;
import hu.szte.brawlers.model.Rarity;
import hu.szte.brawlers.model.dto.ItemDto;
import hu.szte.brawlers.repository.AttributeModifierRepository;
import hu.szte.brawlers.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;
    private final AttributeModifierRepository attributeModifierRepository;
    public List<ItemDto> findAll() {
        return itemRepository.findAll().stream().map(itemConverter::itemToDto).toList();
    }

    public ItemDto getItemByName(String name) {
        return itemConverter.itemToDto(itemRepository.getItemByName(name).orElseThrow());
    }

    public ItemDto addItem(ItemDto itemDto) {
        AttributeModifier attributeModifier = attributeModifierRepository.getReferenceById(itemDto.getAttributeModifierDtos().longValue());
        return itemConverter.itemToDto(itemRepository.save(itemConverter.dtoToItemForSave(itemDto, attributeModifier)));
    }

    public ItemDto updateItem(ItemDto itemDto, String name) {
        Item item = itemRepository.getItemByName(name).orElseThrow();
        return itemConverter.itemToDto(itemRepository.save(itemConverter.updateItem(item, itemDto)));
    }

    public Item getItemByRarityList(Rarity[] rarities, Integer[] percentages) {
        Rarity randomRarity = getRarityByPercentage(rarities,percentages);

        List<Item> items = itemRepository.getItemsByRarity(randomRarity).orElseThrow();

        return items.get(new Random().nextInt(items.size()));
    }

    private Rarity getRarityByPercentage(Rarity[] rarities, Integer[] percentages) {
        if (rarities.length != percentages.length) {
            throw new IllegalArgumentException("Number of rarities must match the percentages");
        }

        int randomNumber = new Random().nextInt(100);

        int cumulativeChances = 0;
        for (int i = 0; i < rarities.length; i++) {
            cumulativeChances += percentages[i];
            if (randomNumber < cumulativeChances) {
                return rarities[i];
            }
        }

        return rarities[0];
    }

    public void deleteItem(String name) {
        itemRepository.deleteByName(name);
    }

    public ItemDto getItemByRarity(Rarity rarity) {
        return itemConverter.itemToDto(itemRepository.getItemByRarity(rarity)
                .orElseThrow());
    }

    public ItemDto dropItem() {
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;

        if (randomNumber <= 60) {
            return getItemByRarity(Rarity.COMMON);
        } else if (randomNumber <= 90) {
            return getItemByRarity(Rarity.RARE);
        } else {
            return getItemByRarity(Rarity.UNIQUE);
        }
    }

}

