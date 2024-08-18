package hu.szte.brawlers.service;

import hu.szte.brawlers.model.Profile;
import hu.szte.brawlers.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileDetailsService implements UserDetailsService {
    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile user = profileRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User name " + username + " not found"));

        User loginUser = (User) User.builder().username(user.getUserName()).password(user.getPassword()).roles(String.valueOf(user.getRoles())).build();
        return loginUser;
    }
}
