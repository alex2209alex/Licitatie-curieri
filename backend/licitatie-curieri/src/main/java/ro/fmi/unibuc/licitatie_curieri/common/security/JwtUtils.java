package ro.fmi.unibuc.licitatie_curieri.common.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JwtUtils {
    private static final String SECRET_KEY = "MOPS";
    private static final long EXPIRATION_TIME = 604800000; // 7 days

    private JwtUtils() {}

    // source: https://www.baeldung.com/java-json-web-tokens-jjwt
    public static String generateToken(Long userId, UserType userType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        byte[] apiKeySecretBytes = Base64.getEncoder().encode(SECRET_KEY.getBytes());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());

        return Jwts.builder()
                .setId(userId.toString())
                .setSubject(userType.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }
}
