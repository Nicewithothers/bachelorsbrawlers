package hu.szte.brawlers.model.dto;

import hu.szte.brawlers.model.Hero;
import hu.szte.brawlers.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProfileDto {
    private String username;
    private String email;
    private Collection<Role> role;
    private Hero hero;
}
