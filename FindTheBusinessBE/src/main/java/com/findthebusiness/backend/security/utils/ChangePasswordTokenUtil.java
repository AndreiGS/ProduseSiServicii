package com.findthebusiness.backend.security.utils;

import com.findthebusiness.backend.security.security_config.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class ChangePasswordTokenUtil {
    private final String secretFromEnvVar = System.getenv("SPRING_APP_TOKEN_SECRET_CHANGE_PASSWORD");

    private final String SECRET_KEY = (secretFromEnvVar == null) ? "secret" : secretFromEnvVar;

    public String extractId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateChangePasswordToken(String userId, Integer timeInMinutes) {
        Map<String, Object> claims = new HashMap<>();
        return createChangePasswordToken(claims, userId, timeInMinutes);
    }

    private String createChangePasswordToken(Map<String, Object> claims, String subject, Integer timeInMinutes) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * timeInMinutes))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /*public boolean validateToken(String token, MyUserDetails userDetails) {
        final String id = extractId(token);
        return (id.equals(userDetails.getId()) && !isTokenExpired(token));
    }*/
}
