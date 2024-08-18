package hu.szte.brawlers.service;

import hu.szte.brawlers.model.Profile;
import hu.szte.brawlers.model.RegistrationRequest;
import hu.szte.brawlers.model.Role;
import hu.szte.brawlers.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationService {
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public String registration(RegistrationRequest registrationRequest) {
        Profile profile = Profile.builder()
                .email(registrationRequest.getEmail())
                .created(LocalDateTime.now())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .userName(registrationRequest.getUserName())
                .roles(Role.USER)
                .build();
        profileRepository.save(profile);
        return "OK";
    }
}
