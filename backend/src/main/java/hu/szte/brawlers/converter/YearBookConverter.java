package hu.szte.brawlers.converter;

import hu.szte.brawlers.model.YearBookEntity;
import hu.szte.brawlers.model.dto.YearBookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class YearBookConverter {
    public YearBookDto yearBookEntityToDto(YearBookEntity yearBookEntity){
        return YearBookDto.builder()
                .heroLevel(yearBookEntity.getHeroLevel())
                .heroId(yearBookEntity.getHeroId())
                .defeatDateTime(yearBookEntity.getDefeatDateTime())
                .build();
    }

    public YearBookEntity yearBookDtoToEntity(YearBookDto yearBookDto){
        return YearBookEntity.builder()
                .heroLevel(yearBookDto.getHeroLevel())
                .heroId(yearBookDto.getHeroId())
                .defeatDateTime(yearBookDto.getDefeatDateTime())
                .build();
    }
}
