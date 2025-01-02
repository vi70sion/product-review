package lt.productreview.product_review.controller;

import lt.productreview.product_review.model.User;
import lt.productreview.product_review.service.AuthorizationService;
import lt.productreview.product_review.service.JwtUtil;
import lt.productreview.product_review.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserDataController {

    @Value("${encryption.secretWord}")
    private String SECRET_WORD;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private UserDataService userDataService;

    @PostMapping("/login")
    public ResponseEntity <Map<String, String>> checkUser(@RequestBody User user) {
        String hashedPassword = userDataService.hashPasswordWithSecret(user.getPassword(), SECRET_WORD);
        user.setPassword(hashedPassword);
        UUID userId = userDataService.validateUser(user);
        if (userId == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        String jwtToken = jwtUtil.generateJwt(userId);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user,
                                        @RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<String> validationResponse = authorizationService.validateAuthorizationHeader(authorizationHeader);
        if (validationResponse != null) {
            return validationResponse;
        }
        ResponseEntity<String> updateResponse = userDataService.updateUser(user, authorizationHeader);
        if (updateResponse != null) {
            return updateResponse;
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success.");
    }

    @GetMapping("/name")
    public ResponseEntity<?> getUserNameById(@RequestParam String id,
                                             @RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<String> validationResponse = authorizationService.validateAuthorizationHeader(authorizationHeader);
        if (validationResponse != null) {
            return validationResponse;
        }
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            //error: wrong UUID format
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UUID format.");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDataService.getUserNameById(userId));
    }

}
