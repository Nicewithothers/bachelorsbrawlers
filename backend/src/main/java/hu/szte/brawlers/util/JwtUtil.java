package hu.szte.brawlers.util;

import hu.szte.brawlers.model.AuthResponse;
import hu.szte.brawlers.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Function;



@Service
public class JwtUtil {
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private TokenRepository tokenRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public List<GrantedAuthority> extractRole(String token) {
        final Claims claims = extractAllClaims(token);
        return (List<GrantedAuthority>) claims.get("auth");
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails userDetails) {
        return createToken(userDetails);
    }

    private String createToken(UserDetails userDetails) {

        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        AuthResponse authResponse = new AuthResponse(token, userDetails.getAuthorities().toString());
        tokenRepository.save(authResponse);
        return token;
    }

    @Cacheable(value = "tokenCache", key = "#token")
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        containsAuth(token, userDetails);
        return (username.equals(userDetails.getUsername()) && tokenRepository.existsAuthResponseByToken(token) && containsAuth(token, userDetails));
    }

    public boolean containsAuth(String token, UserDetails userDetails) {
        AuthResponse authResponse = tokenRepository.findAuthResponseByToken(token);
        return userDetails.getAuthorities().toString().contains(authResponse.getRole().substring(1, authResponse.getRole().length() - 1));
    }

    public void inValidateToken(String token) {
        tokenRepository.delete(tokenRepository.findAuthResponseByTokenContains(token.substring(7)));
        cacheManager.getCache("tokenCache").clear();
    }


}

