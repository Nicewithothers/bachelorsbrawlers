package hu.szte.brawlers.service;

import hu.szte.brawlers.converter.YearBookConverter;
import hu.szte.brawlers.model.AttributeModifier;
import hu.szte.brawlers.model.dto.ItemDto;
import hu.szte.brawlers.model.dto.YearBookDto;
import hu.szte.brawlers.repository.YearBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YearBookService {
    private final YearBookRepository yearBookRepository;
    private final YearBookConverter yearBookConverter;


    public List<YearBookDto> findAll() {
        return yearBookRepository.findAll()
                .stream()
                .map(yearBookConverter::yearBookEntityToDto)
                .toList();
    }

    public YearBookDto addYearBook(YearBookDto yearBookDto) {
        return yearBookConverter.yearBookEntityToDto(yearBookRepository.save(yearBookConverter.yearBookDtoToEntity(yearBookDto)));
    }
}
