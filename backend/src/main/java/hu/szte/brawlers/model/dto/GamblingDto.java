package hu.szte.brawlers.model.dto;


import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class GamblingDto {
    private String serial;
    private Set<Integer> playerNumbers;
    private List<Integer> pyramid1Numbers;
    private List<Integer> pyramid2Numbers;
    private List<Integer> pyramid3Numbers;
    private Integer allWinnings;
}
