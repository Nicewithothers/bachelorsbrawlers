package hu.szte.brawlers.mapper;

import hu.szte.brawlers.model.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthMapper {

    public Collection<Role> roleMapper(String adRole) {
        Collection<Role> roles = new ArrayList<>();
        String[] roleStrings = adRole.split(", ");
        for (String role : roleStrings) {
            if (role.contains(Role.ADMIN.toString()))
                roles.add(Role.ADMIN);
            if (role.contains(Role.USER.toString()))
                roles.add(Role.USER);
        }
        return roles;
    }

}
