package hu.szte.brawlers.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class YearBookDto {
    private Integer heroLevel;
    private LocalDateTime defeatDateTime;
    private Long heroId;
}
