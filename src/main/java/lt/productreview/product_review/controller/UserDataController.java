package lt.productreview.product_review.controller;

import lt.productreview.product_review.model.User;
import lt.productreview.product_review.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserDataController {

    @Autowired
    private JwtUtil jwtUtil;

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
