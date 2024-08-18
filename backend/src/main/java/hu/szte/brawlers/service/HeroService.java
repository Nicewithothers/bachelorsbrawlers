package hu.szte.brawlers.service;

import hu.szte.brawlers.InsufficientFundsException;
import hu.szte.brawlers.converter.HeroConverter;
import hu.szte.brawlers.model.Hero;
import hu.szte.brawlers.model.Item;
import hu.szte.brawlers.model.Profile;
import hu.szte.brawlers.model.dto.GamblingDto;
import hu.szte.brawlers.model.dto.ItemDto;
import hu.szte.brawlers.repository.ItemRepository;
import hu.szte.brawlers.model.dto.HeroDto;
import hu.szte.brawlers.repository.HeroRepository;
import hu.szte.brawlers.repository.ProfileRepository;
import hu.szte.brawlers.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;
    private final HeroConverter heroConverter;
    private final ItemRepository itemRepository;
    private final AttributeModifierService attributeModifierService;
    private final ProfileRepository profileRepository;
    private final JwtUtil jwtUtil;
    private final SimpMessagingTemplate messagingTemplate;
    private final MinioService minioService;

    public HeroDto HeroByProfile(String token) {
        String userName = jwtUtil.extractUsername(token);
        Profile profile = profileRepository.findByUserName(userName).orElseThrow();
        HeroDto heroDto = heroConverter.heroToDto(heroRepository.getHeroByProfile(profile).orElseThrow());
        String fileName = userName + ".png";
        heroDto.setImage(fileName);
        byte[] profilePicture = minioService.downloadFile("bachelorsbrawlers", fileName);
        heroDto.setPicture(profilePicture);
        return heroDto;
    }

    public Integer dungeonLevelOfHero(String token) {
        String userName = jwtUtil.extractUsername(token);
        return heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow().getDungeonLevel();
    }


    public List<HeroDto> findAll() {
        return heroRepository.findAll().stream().map(heroConverter::heroToDto).collect(Collectors.toList());
    }

    public List<HeroDto> findByXpDescending() {
        return heroRepository.findAllByOrderByXpDesc()
                .stream()
                .map(hero -> {
                    HeroDto heroDto = heroConverter.heroToDto(hero);
                    byte[] fileData = minioService.downloadFile("bachelorsbrawlers", hero.getProfile().getUserName() + ".png");
                    heroDto.setPicture(fileData);
                    return heroDto;
                })
                .collect(Collectors.toList());
    }

    public HeroDto addHero(HeroDto heroDto) {
        Profile profile = profileRepository.findByUserName(heroDto.getProfileName()).orElseThrow();
        return heroConverter.heroToDto(heroRepository.save(heroConverter.createDtoToHero(heroDto, profile)));
    }

    public HeroDto updateHero(HeroDto heroDto, String heroName) {
        Hero hero = heroRepository.getHeroByName(heroName).orElseThrow();
        return heroConverter.heroToDto(heroRepository.save(heroConverter.updateHero(heroDto, hero)));
    }

    public boolean deleteHero(String heroName) {
        return heroRepository.deleteHeroByName(heroName);
    }

    public Integer updateHeroEndurance(String name, Integer change) {
        Hero hero = heroRepository.getHeroByName(name).orElseThrow();
        hero.setEndurance(hero.getEndurance() + change);
        heroRepository.save(hero);
        return hero.getEndurance();
    }

    public Integer updateHeroForint(String name, Integer change) {
        Hero hero = heroRepository.getHeroByName(name).orElseThrow();
        hero.setForint(hero.getForint() + change);
        heroRepository.save(hero);
        return hero.getForint();
    }

    public Integer updateHeroCrypto(String name, Integer change) {
        Hero hero = heroRepository.getHeroByName(name).orElseThrow();
        hero.setCrypto(hero.getCrypto() + change);
        heroRepository.save(hero);
        return hero.getCrypto();
    }


    public HeroDto equipItem(String token, String itemName) {
        String userName = jwtUtil.extractUsername(token);
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();
        Item item = itemRepository.getItemByName(itemName).orElseThrow();

        hero.getEquipmentItems()
                .stream()
                .filter(item1 -> item1.getItemCategory().equals(item.getItemCategory()))
                .findFirst()
                .ifPresentOrElse(
                        value -> {
                            hero.getEquipmentItems().remove(value);
                            hero.getBackpackItems().add(value);
                            hero.getBackpackItems().remove(item);
                            hero.getEquipmentItems().add(item);
                        }, () -> {
                            hero.getBackpackItems().remove(item);
                            hero.getEquipmentItems().add(item);
                        });
        return heroConverter.heroToDto(heroRepository.save(hero));

    }

    @Transactional
    public HeroDto unequipItem(String token, String itemName) throws Exception {
        String userName = jwtUtil.extractUsername(token);
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();
        Item item = itemRepository.getItemByName(itemName).orElseThrow();

        if (hero.getBackpackItems() != null && hero.getBackpackItems().size() > 10) {
            throw new Exception("Nincs elég hely a hátizsákban!");
        }

        hero.getEquipmentItems()
                .stream()
                .filter(item1 -> item1.getName().equals(itemName))
                .findFirst()
                .ifPresent(value -> {
                            hero.getEquipmentItems().remove(item);
                            hero.getBackpackItems().add(item);
                        }
                );

        return heroConverter.heroToDto(heroRepository.save(hero));
    }

    public HeroDto lootItem(String token, ItemDto itemDto) {
        String userName = jwtUtil.extractUsername(token);
        Hero hero = heroRepository.getHeroByProfile(profileRepository
                        .findByUserName(userName)
                        .orElseThrow())
                .orElseThrow();
        Item item = itemRepository.getItemByName(itemDto.getName()).orElseThrow();

        List<Item> currentItems = hero.getBackpackItems();
        currentItems.add(item);
        hero.setBackpackItems(currentItems);

        return heroConverter.heroToDto(heroRepository.save(hero));

    }

    public int calculateFibonacci(int n) {
        int a = 0, b = 1, c;
        if (n == 0) {
            return a;
        }
        for (int i = 2; i <= n; i++) {
            c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    public HeroDto increaseHeroStat(String token, String statName) throws Exception {
        String userName = jwtUtil.extractUsername(token);
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();
        try {
            int level = switch (statName) {
                case "endurance" -> hero.getEndurance();
                case "diligence" -> hero.getDiligence();
                case "intelligence" -> hero.getIntelligence();
                case "dexterity" -> hero.getDexterity();
                case "luck" -> hero.getLuck();
                default -> throw new IllegalArgumentException("Invalid stat name");
            };
            int cost = calculateFibonacci(level + 1);

            if (hero.getForint() < cost) {
                throw new InsufficientFundsException("Not enough Forint for stat increase");
            }

            hero.setForint(hero.getForint() - cost);

            switch (statName) {
                case "endurance" -> hero.setEndurance(level + 1);
                case "diligence" -> hero.setDiligence(level + 1);
                case "intelligence" -> hero.setIntelligence(level + 1);
                case "dexterity" -> hero.setDexterity(level + 1);
                case "luck" -> hero.setLuck(level + 1);
                default -> throw new IllegalArgumentException("Invalid stat name");
            }
            messagingTemplate.convertAndSend("/topic/forint-updates", hero.getForint());
            return heroConverter.heroToDto(heroRepository.save(hero));
        } catch (Exception e) {
            throw new Exception("Nem jött össze a dolog!");
        }
    }

    public Integer getHeroStatLevel(String token, String statName) {
        String userName = jwtUtil.extractUsername(token);
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();
        return switch (statName) {
            case "endurance" -> hero.getEndurance();
            case "diligence" -> hero.getDiligence();
            case "intelligence" -> hero.getIntelligence();
            case "dexterity" -> hero.getDexterity();
            case "luck" -> hero.getLuck();
            default -> throw new IllegalArgumentException("Invalid stat name: " + statName);
        };
    }

    // Generate player number - 20 unique number
    private Set<Integer> generatePlayerNumbers() {

        Set<Integer> playerNumbers = new HashSet<>();
        Random random = new Random();

        while (playerNumbers.size() < 20) {
            Integer newNumber = random.nextInt(99) + 1;
            playerNumbers.add(newNumber);
        }
        return playerNumbers;
    }

    private boolean calculateChance(double probability) {
        Random random = new Random();
        double randomNumber = random.nextDouble();
        return randomNumber < probability;
    }

    public GamblingDto gambling(String token) {

        String userName = jwtUtil.extractUsername(token);
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();

        if (hero.getForint() < 200) {
            throw new InsufficientFundsException("Nincs elég pénzed a szerencsejátékhoz!");
        }
        hero.setForint(hero.getForint() - 200);
        messagingTemplate.convertAndSend("/topic/forint-updates", hero.getForint());

        Set<Integer> playerNumbers = this.generatePlayerNumbers();

        List<Integer> pyramid1 = new ArrayList<>();
        List<Integer> pyramid2 = new ArrayList<>();
        List<Integer> pyramid3 = new ArrayList<>();

        double[] probabilities = {0.15, 0.05, 0.025, 0.01, 0.005, 0.0025};
        Integer[] wins = {200, 500, 1000, 2000, 5000, 50000};
        Integer allWinnings = 0;

        //Build pyramid 1 (5 rows - 15 numbers)
        for (int i = 0; i < 5; i++) {
            if (this.calculateChance(probabilities[i])) {
                pyramid1.addAll(this.getCorrectNumbers(playerNumbers, i + 1));
                allWinnings += wins[i];
                //Add win here
            } else {
                if (i > 0) {
                    var newNumbers = new ArrayList<>(this.getIncorrectNumbers(playerNumbers, 1).stream().toList());
                    newNumbers.addAll(this.getCorrectNumbers(playerNumbers, i));
                    Collections.shuffle(newNumbers);
                    pyramid1.addAll(newNumbers);
                } else {
                    pyramid1.addAll(this.getIncorrectNumbers(playerNumbers, i + 1));
                }
            }
        }

        //Build pyramid 2 (4 rows - 10 numbers)
        for (int i = 0; i < 4; i++) {
            if (this.calculateChance(probabilities[i])) {
                pyramid2.addAll(this.getCorrectNumbers(playerNumbers, i + 1));
                //Add win here
                allWinnings += wins[i];
            } else {
                if (i > 0) {
                    var newNumbers = new ArrayList<>(this.getIncorrectNumbers(playerNumbers, 1).stream().toList());
                    newNumbers.addAll(this.getCorrectNumbers(playerNumbers, i));
                    Collections.shuffle(newNumbers);
                    pyramid2.addAll(newNumbers);
                } else {
                    pyramid2.addAll(this.getIncorrectNumbers(playerNumbers, i + 1));
                }
            }
        }

        //Build pyramid 3 (6 rows - 21 numbers)
        for (int i = 0; i < 6; i++) {
            if (this.calculateChance(probabilities[i])) {
                pyramid3.addAll(this.getCorrectNumbers(playerNumbers, i + 1));
                allWinnings += wins[i];
            } else {
                if (i > 0) {
                    var newNumbers = new ArrayList<>(this.getIncorrectNumbers(playerNumbers, 1).stream().toList());
                    newNumbers.addAll(this.getCorrectNumbers(playerNumbers, i));
                    Collections.shuffle(newNumbers);
                    pyramid3.addAll(newNumbers);
                } else {
                    pyramid3.addAll(this.getIncorrectNumbers(playerNumbers, i + 1));
                }
            }
        }

        hero.setLastWin(allWinnings);
        heroRepository.save(hero);

        GamblingDto gamblingDto = new GamblingDto();
        gamblingDto.setSerial("1234-5678-1111-0000");
        gamblingDto.setPlayerNumbers(playerNumbers);
        gamblingDto.setPyramid1Numbers(pyramid1);
        gamblingDto.setPyramid2Numbers(pyramid2);
        gamblingDto.setPyramid3Numbers(pyramid3);
        gamblingDto.setAllWinnings(allWinnings);
        return gamblingDto;
    }

    //Return random number from the set
    private Set<Integer> getCorrectNumbers(Set<Integer> playerNumbers, int numberCnt) {
        Set<Integer> result = new HashSet<>();
        List<Integer> numberList = new ArrayList<>(playerNumbers);
        Collections.shuffle(numberList);
        while (result.size() < numberCnt) {
            result.add(numberList.get(0));
            numberList.remove(0);
        }
        return result;
    }

    //Return only incorrect numbers
    private Set<Integer> getIncorrectNumbers(Set<Integer> playerNumbers, int numberCnt) {
        Set<Integer> incorrectNumbers = new HashSet<>();
        Random random = new Random();
        while (incorrectNumbers.size() < numberCnt) {
            Integer newNumber = random.nextInt(99) + 1;
            if (!playerNumbers.contains(newNumber)) {
                incorrectNumbers.add(newNumber);
            }
        }
        return incorrectNumbers;
    }

    public Integer claimVictory(String token) {
        String userName = jwtUtil.extractUsername(token);
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();

        if (hero.getLastWin() != -1) {
            hero.setForint(hero.getForint() + hero.getLastWin());
            messagingTemplate.convertAndSend("/topic/forint-updates", hero.getForint());
            hero.setLastWin(-1);
            heroRepository.save(hero);
        }

        return hero.getForint();
    }

    public Integer getLastWin(String token) {
        String userName = jwtUtil.extractUsername(token);
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();

        return hero.getLastWin();
    }

    public Integer getDungeonLevel(String token) {
        String userName = jwtUtil.extractUsername(token);
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();
        return hero.getDungeonLevel();
    }

    public LocalDateTime getNextTryTime(String token) {
        String userName = jwtUtil.extractUsername(token);
        Hero hero = heroRepository.getHeroByProfile(profileRepository.findByUserName(userName).orElseThrow()).orElseThrow();
        return hero.getNextDungeonTry();
    }

    public void handleLevelUp(String name, Long xp) {
        Hero hero = heroRepository.getHeroByName(name).orElseThrow();

        // Kiszámítjuk a maradék xp-t
        long excessXpMultipleLevel = xp - hero.getMaxXp();

        //Ha ez az xp nagyobb mint 0, akkor kiszámítjuk mennyi szintet kell ugrani, eszerint korrigáljuk az értékeket.
        if (excessXpMultipleLevel > 0) {
            int levelsToJump = (int) Math.ceil(excessXpMultipleLevel / hero.getMaxXp());
            hero.setLevel(hero.getLevel() + levelsToJump);
            hero.setXp(excessXpMultipleLevel % hero.getMaxXp());
            hero.setMaxXp((long) (hero.getMaxXp() * (Math.pow(hero.getLevel(), 1.5))));
            //Statok növelése automatikusan szintlépéskor, korrigálva a szintugrásokkal.
            hero.setEndurance(hero.getEndurance() + levelsToJump * 3);
            hero.setDiligence(hero.getEndurance() + levelsToJump * 2);
            hero.setDexterity(hero.getEndurance() + levelsToJump * 2);
            hero.setIntelligence(hero.getEndurance() + levelsToJump);
        } else {
            //Ha nem, akkor nincs maradék xp, rögtön hozzáadjuk a többihez.
            hero.setXp(xp + hero.getXp());
            if (hero.getXp() >= hero.getMaxXp()) {
                hero.setLevel(hero.getLevel() + 1);
                hero.setXp(hero.getXp() - hero.getMaxXp());
                hero.setMaxXp((long) (hero.getMaxXp() * (Math.pow(hero.getLevel(), 1.5))));
                //Statok növelése automatikusan szintlépéskor, korrigálva a szintugrásokkal.
                hero.setEndurance(hero.getEndurance() + 3);
                hero.setDiligence(hero.getEndurance() + 2);
                hero.setDexterity(hero.getEndurance() + 2);
                hero.setIntelligence(hero.getEndurance() + 1);
            }
        }
    }

    public List<HeroDto> getHerosByIds(String token, List<String> ids) {

        List<HeroDto> result = new ArrayList<>();
        List<Hero> heroes = heroRepository.findAllByIdIn(ids);
        heroes.forEach(hero -> result.add(heroConverter.heroToDto(hero)));

        return result;
    }
}
