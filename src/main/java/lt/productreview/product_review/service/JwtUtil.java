package lt.productreview.product_review.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtUtil {

    @Value("${encryption.secretKey}")
    private String SECRET_KEY;

    public String generateJwt(String username, String password, int userId) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + 120 * 60 * 1000); // 120 minutes

        String jwt = Jwts.builder()
                .setIssuer("manokompanija.eu")
                .setSubject("manokompanija.eu")
                .claim("Username", username)
                .claim("Password", password)
                .claim("UserId", userId)
                .claim("DateOfLogin", new SimpleDateFormat("yyyy-MM-dd").format(now))
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return jwt;
    }

    public Claims decodeJwt(String jwt) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return claims;
    }


    public Boolean validateToken(String token){
        try {
            decodeJwt(token);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }


}
