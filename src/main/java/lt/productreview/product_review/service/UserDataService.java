package lt.productreview.product_review.service;

import lt.productreview.product_review.model.User;
import lt.productreview.product_review.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class UserDataService {

    @Value("${encryption.secretWord}")
    private String SECRET_WORD;

    @Autowired
    UserDataRepository userDataReposirory;
    @Autowired
    private JwtUtil jwtUtil;

    public UUID validateUser(User user) {
        return userDataReposirory.validateUser(user);
    }

    public ResponseEntity<String> updateUser(User user, String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        UUID userId = jwtUtil.userIdFromToken(token);
        if (userDataReposirory.getUserNameById(userId).equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        user.setId(userId);
//        String hashedPassword = hashPasswordWithSecret(user.getPassword(), SECRET_WORD);
//        user.setPassword(hashedPassword);
        boolean isUpdated = userDataReposirory.updateUser(user);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body("Success.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed.");
        }
    }

    public String getUserNameById(UUID id) {
        return userDataReposirory.getUserNameById(id);
    }

    public String hashPasswordWithSecret(String password, String secretWord) {
        try {
            // Combine the password with the secret word
            String combined = password + secretWord;
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Hash the combined string
            byte[] hash = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
            // Convert the hash to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

}
