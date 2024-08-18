package hu.szte.brawlers.converter;

import hu.szte.brawlers.mapper.AuthMapper;
import hu.szte.brawlers.model.Role;
import hu.szte.brawlers.model.dto.ProfileDto;
import hu.szte.brawlers.repository.TokenRepository;
import hu.szte.brawlers.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor

public class UserConverter {
    private final JwtUtil jwtTokenUtil;
    private final TokenRepository tokenRepository;
    private final AuthMapper authMapper;

    public ProfileDto tokenToDto(String token) {
        Collection<Role> roles = new ArrayList<>();
        roles = (authMapper.roleMapper(tokenRepository.findAuthResponseByToken(token).getRole()));
        return ProfileDto.builder().username(jwtTokenUtil.extractUsername(token)).role(roles).build();
    }

}
