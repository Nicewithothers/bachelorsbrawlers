package hu.szte.brawlers.service;

import hu.szte.brawlers.converter.UserConverter;
import hu.szte.brawlers.model.AuthRequest;
import hu.szte.brawlers.model.RegistrationRequest;
import hu.szte.brawlers.model.dto.ProfileDto;
import hu.szte.brawlers.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final UserConverter userConverter;

    public String authenticate(AuthRequest authRequestString) throws Exception {
        Objects.requireNonNull(authRequestString.getUsername());
        Objects.requireNonNull(authRequestString.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestString.getUsername(), authRequestString.getPassword()));
            return this.generateToken((UserDetails) authentication.getPrincipal());
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public String generateToken(UserDetails userDetails) {
        return jwtTokenUtil.generateToken(userDetails);
    }

    public void inValidateToken(String token) {
        jwtTokenUtil.inValidateToken(token);
    }

    public ProfileDto getUserDataFromToken(String token) {
        return userConverter.tokenToDto(token);

    }


}
