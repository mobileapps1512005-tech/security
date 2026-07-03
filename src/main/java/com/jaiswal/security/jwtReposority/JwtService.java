package com.jaiswal.security.jwtReposority;

import com.jaiswal.security.UserEntity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtService {

    private final String SECRET = "mysecretkeymysecretkeymysecretkey123456";// >= 32 chars
    public final long RegisterExpireTime=15L*60*60*1000;
    public final long LoginExpireTime=15L*60*60*1000;
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(Users users ,long expire) {

        return Jwts.builder()
                .subject(users.getEmail())
                .claim("role",users.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expire)) // 1 hour
                .signWith(getKey())
                .compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        return getClaims(token).getExpiration().after(new Date());
    }


    private Claims getClaims(String token) {

        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT token is missing");
        }

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
