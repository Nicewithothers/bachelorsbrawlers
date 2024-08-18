package hu.szte.brawlers.repository;

import hu.szte.brawlers.model.AuthResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<AuthResponse, Long> {
    public boolean existsAuthResponseByToken(String token);

    public AuthResponse findAuthResponseByTokenContains(String s);

    public boolean existsAuthResponseByTokenAndAndRole(String token, String role);

    public AuthResponse findAuthResponseByToken(String token);

}
