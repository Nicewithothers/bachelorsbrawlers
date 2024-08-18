package hu.szte.brawlers.service;

import hu.szte.brawlers.converter.AttributeModifierConverter;
import hu.szte.brawlers.model.dto.AttributeModifierDto;
import hu.szte.brawlers.model.dto.BoosterItemDto;
import hu.szte.brawlers.model.dto.ItemDto;
import hu.szte.brawlers.model.dto.ItemSeederDto;
import hu.szte.brawlers.model.dto.MonsterDto;
import hu.szte.brawlers.repository.AttributeModifierRepository;
import hu.szte.brawlers.repository.ItemRepository;
import hu.szte.brawlers.repository.MonsterRepository;
import hu.szte.brawlers.repository.TobaccoShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeederService {
    private final AttributeModifierService attributeModifierService;
    private final ItemService itemService;
    private final MonsterService monsterService;
    private final ItemRepository itemRepository;
    private final AttributeModifierRepository attributeModifierRepository;
    private final MonsterRepository monsterRepository;
    private final TobaccoShopService tobaccoShopService;
    private final TobaccoShopRepository tobaccoShopRepository;

    private <T> List<T> parseCsv(Class<T> type, String resourceName) {
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(resourceName);

        try (Reader reader = new InputStreamReader(resourceStream)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runItemSeeder() {

        List<ItemSeederDto> itemList = parseCsv(ItemSeederDto.class, "seed/Item.csv");

        List<ItemDto> items = itemList.stream().map(this::seederDtoToDto).toList();

        for (ItemDto dto : items) {
            itemService.addItem(dto);
        }
    }

    public void runAttributeModifierSeed() {
        List<AttributeModifierDto> AttributeModifierList = parseCsv(AttributeModifierDto.class,
                "seed/AttributeModifier.csv");

        for (AttributeModifierDto dto : AttributeModifierList) {
            attributeModifierService.addAttributeModifier(dto);
        }
    }

    private ItemDto seederDtoToDto(ItemSeederDto itemSeederDto) {
        return ItemDto.builder()
                .armor(itemSeederDto.getArmor())
                .damageRange(itemSeederDto.getDamageRange())
                .itemCategory(itemSeederDto.getItemCategory())
                .price(itemSeederDto.getPrice())
                .name(itemSeederDto.getName())
                .rarity(itemSeederDto.getRarity())
                .attributeModifierDtos(itemSeederDto.getAttributeModifierId())
                .fileName(itemSeederDto.getFileName())
                .build();
    }

    public void runMonsterSeeder() {
        List<MonsterDto> MonsterList = parseCsv(MonsterDto.class, "seed/Monster.csv");

        for (MonsterDto dto : MonsterList) {
            monsterService.addMonster(dto);
        }
    }

    public void runBoosterItemSeeder() {
        List<BoosterItemDto> BoosterItemList = parseCsv(BoosterItemDto.class, "seed/BoosterItem.csv");

        for (BoosterItemDto dto : BoosterItemList) {
            tobaccoShopService.addBoosterItem(dto);
        }
    }

    public void deleteAllItems() {
        itemRepository.deleteAll();
    }

    public void deleteAllAttributeModifiers() {
        attributeModifierRepository.deleteAll();
    }

    public void deleteAllMonsters() {
        monsterRepository.deleteAll();
    }

    public void deleteAllBoosterItems() {
        tobaccoShopRepository.deleteAll();;
    }
}


