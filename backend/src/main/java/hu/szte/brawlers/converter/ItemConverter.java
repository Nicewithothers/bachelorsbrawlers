package hu.szte.brawlers.converter;

import hu.szte.brawlers.model.AttributeModifier;
import hu.szte.brawlers.model.Item;
import hu.szte.brawlers.model.dto.ItemDto;
import hu.szte.brawlers.repository.AttributeModifierRepository;
import hu.szte.brawlers.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ItemConverter {
    private final AttributeModifierConverter attributeModifierConverter;
    private final AttributeModifierRepository attributeModifierRepository;
    private final MinioService minioService;

    public Item dtoToItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .itemCategory(itemDto.getItemCategory())
                .rarity(itemDto.getRarity())
                .armor(itemDto.getArmor())
                .damageRange(itemDto.getDamageRange())
                .price(itemDto.getPrice())
                .attributeModifiers(itemDto.getAttributeModifierDtos() != null
                        ? attributeModifierRepository.getReferenceById(itemDto.getAttributeModifierDtos().longValue())
                        : null)
                .fileName(itemDto.getFileName())
                .build();
    }

    public Item dtoToItemForSave(ItemDto itemDto, AttributeModifier attributeModifier) {
        return Item.builder()
                .name(itemDto.getName())
                .itemCategory(itemDto.getItemCategory())
                .rarity(itemDto.getRarity())
                .armor(itemDto.getArmor())
                .damageRange(itemDto.getDamageRange())
                .price(itemDto.getPrice())
                .attributeModifiers(attributeModifier)
                .fileName(itemDto.getFileName())
                .build();
    }

    public ItemDto itemToDto(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .itemCategory(item.getItemCategory())
                .rarity(item.getRarity())
                .armor(item.getArmor())
                .damageRange(item.getDamageRange())
                .price(item.getPrice())
                .attributeModifierDtos(
                        item.getAttributeModifiers() != null ? item.getAttributeModifiers().getId().intValue() : null)
                .attributeModifiers(item.getAttributeModifiers())
                .fileName(item.getFileName())
                .build();
    }

    public ItemDto itemToDtoWithPics(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .name(item.getName())
                .itemCategory(item.getItemCategory())
                .rarity(item.getRarity())
                .armor(item.getArmor())
                .damageRange(item.getDamageRange())
                .price(item.getPrice())
                .attributeModifiers(item.getAttributeModifiers())
                .fileName(item.getFileName())
                .build();
        // byte[] fileData = minioService.downloadFile("bachelorsbrawlers","items_webp/" + item.getFileName() + ".webp");
        // itemDto.setPicture(fileData);
        itemDto.setImageUrl(minioService.getPresignedObjectUrl("bachelorsbrawlers","items_webp/" + item.getFileName() + ".webp"));
        return itemDto;
    }

    public Item updateItem(Item item, ItemDto itemDto) {
        item.setName(itemDto.getName());
        item.setItemCategory(itemDto.getItemCategory());
        item.setArmor(itemDto.getArmor());
        item.setRarity(itemDto.getRarity());
        item.setDamageRange(itemDto.getDamageRange());
        item.setPrice(itemDto.getPrice());
        item.setAttributeModifiers(itemDto.getAttributeModifierDtos() != null
                ? attributeModifierRepository.getReferenceById(itemDto.getAttributeModifierDtos().longValue())
                : null);
        item.setFileName(itemDto.getFileName());
        return item;
    }
}
