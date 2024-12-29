package lt.productreview.product_review.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtUtil {

    @Value("${encryption.secretKey}")
    private String SECRET_KEY;

    private Key key;

    @PostConstruct
    public void initKey() {
        // Decode Base64 key
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwt(UUID userId) {
        //Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
//        Key key = Keys.hmacShaKeyFor(keyBytes);

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + 24 * 60 * 60 * 1000); // 24 hours

        String jwt = Jwts.builder()
                .setIssuer("productreview.lt")
                .setSubject("productreview.lt")
                .claim("UserId", userId)
                .claim("DateOfLogin", new SimpleDateFormat("yyyy-MM-dd").format(now))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return jwt;
    }

    public Claims decodeJwt(String jwt) {
        //Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

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
