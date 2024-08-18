package hu.szte.brawlers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Damage {
    private boolean crit;
    private Integer value;
}
