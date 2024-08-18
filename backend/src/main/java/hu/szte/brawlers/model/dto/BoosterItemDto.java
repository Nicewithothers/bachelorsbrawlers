package hu.szte.brawlers.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoosterItemDto {
    private String name;
    private String description;
    private Double effectiveness;
    private Integer price;
}
