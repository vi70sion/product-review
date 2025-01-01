package lt.productreview.product_review.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<String> validateAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Authorization header must be provided and start with 'Bearer '.");
        }
        String token = authorizationHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired JWT token.");
        }
        return null; // if ok
    }
}

