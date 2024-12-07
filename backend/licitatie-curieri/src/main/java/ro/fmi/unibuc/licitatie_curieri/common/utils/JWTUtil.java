package ro.fmi.unibuc.licitatie_curieri.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    // Secretul folosit pentru a semna și valida tokenurile
    private final String SECRET_KEY = "secret";

    // Generează un token JWT pentru un utilizator
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Setează utilizatorul ca subiect al tokenului
                .setIssuedAt(new Date()) // Data emiterii
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiră în 10 ore
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Semnătura folosind algoritmul HS256
                .compact(); // Crează tokenul
    }

    // Validarea tokenului JWT
    public String validateToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY) // Secretul pentru validare
                .parseClaimsJws(token) // Parsează tokenul
                .getBody();

        return claims.getSubject(); // Returnează subiectul (username-ul utilizatorului)
    }
}
