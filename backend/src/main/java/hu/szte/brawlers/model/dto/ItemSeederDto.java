package hu.szte.brawlers.model.dto;

import hu.szte.brawlers.model.ItemCategory;
import hu.szte.brawlers.model.Rarity;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ItemSeederDto {
    private ItemCategory itemCategory;
    private String name;
    private Rarity rarity;
    private Integer damageRange;
    private Integer armor;
    private Integer price;
    @Nullable
    private Integer attributeModifierId;
    private String fileName;
}

