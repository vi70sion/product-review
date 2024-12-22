package lt.productreview.product_review.controller;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lt.productreview.product_review.model.User;
import lt.productreview.product_review.service.JwtUtil;
import lt.productreview.product_review.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserDataController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDataService userDataService;


    @PostMapping("/login")
    public ResponseEntity <Map<String, String>> checkUser(@RequestBody User user) {
        UUID userId = userDataService.validateUser(user);
        if (userId == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        String jwtToken = jwtUtil.generateJwt(userId);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user,
                                        @RequestHeader("Authorisation") String authorizationHeader) {
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body("Authorization header must be provided and start with 'Bearer '.");
        }
        String token = authorizationHeader.substring(7);
        if(!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid or expired JWT token.");
        }

        //todo update user info in repository

        // kad nesinervuotų
        return ResponseEntity.status(200).body("Success");
    }


}
