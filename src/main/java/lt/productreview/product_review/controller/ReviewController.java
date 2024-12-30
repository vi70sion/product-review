package lt.productreview.product_review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lt.productreview.product_review.model.Review;
import lt.productreview.product_review.repository.CategoriesRepository;
import lt.productreview.product_review.service.JwtUtil;
import lt.productreview.product_review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CategoriesRepository categoriesRepository;

    @GetMapping("/category")
    public ResponseEntity<?> getReviewsByCategory(@RequestParam String category,
                                          @RequestHeader("Authorization") String authorizationHeader) {
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Authorization header must be provided and start with 'Bearer '.");
        }
        String token = authorizationHeader.substring(7);
        if(!jwtUtil.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired JWT token.");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reviewService.getReviewsByCategory(category));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestPart("review") String reviewJson,
                                       @RequestParam("image") MultipartFile image,
                                       @RequestParam("category") String category,
                                       @RequestHeader("Authorization") String authorizationHeader) {
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body("Authorization header must be provided and start with 'Bearer '.");
        }
        String token = authorizationHeader.substring(7);
        if(!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid or expired JWT token.");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Review review = objectMapper.readValue(reviewJson, Review.class);
            byte[] reviewPhoto = image.getBytes();   // image to byte array
            review.setPhoto(reviewPhoto);
            review.setCategoryId(categoriesRepository.getCategoryIdFromName(category));

            // user ID from JWT
            Claims claims = jwtUtil.decodeJwt(token);
            String userIdString = claims.get("UserId", String.class);
            review.setUserId(UUID.fromString(userIdString));

            if (reviewService.addReview(review)) return ResponseEntity
                                                            .status(HttpStatus.OK)
                                                            .body("success");
        } catch (IOException e) {
            // error;
        }
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("failed");
    }


}
