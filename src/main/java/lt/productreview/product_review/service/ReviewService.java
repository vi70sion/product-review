package lt.productreview.product_review.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lt.productreview.product_review.controller.MessagingController;
import lt.productreview.product_review.model.Review;
import lt.productreview.product_review.model.Role;
import lt.productreview.product_review.repository.CategoriesRepository;
import lt.productreview.product_review.repository.ReviewRepository;
import lt.productreview.product_review.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MessagingController messagingController;

    public List<Review> getReviewsByCategory(String category) {
        return reviewRepository.getReviewsByCategory(category);
    }

    public boolean addReview(String reviewJson, MultipartFile image, String category, String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7);
            ObjectMapper objectMapper = new ObjectMapper();
            Review review = objectMapper.readValue(reviewJson, Review.class);
            byte[] reviewPhoto = image.getBytes();   // image to byte array
            review.setPhoto(reviewPhoto);
            review.setCategoryId(categoriesRepository.getCategoryIdFromName(category));
            review.setCreatedAt(LocalDateTime.now());

            // user ID from JWT
            Claims claims = jwtUtil.decodeJwt(token);
            String userIdString = claims.get("UserId", String.class);
            review.setUserId(UUID.fromString(userIdString));

            boolean isSaved = reviewRepository.addReview(review);
            if (isSaved) {
                String reviewString = redisService.addReviewToRedis(review);
                messagingController.sendMessageToTopic(reviewString);
            }
            return isSaved;
        } catch (IOException e) {
            // error;
        }
        return false;
    }

    public ResponseEntity<String> deleteReviewById(int reviewId, String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        UUID userId = jwtUtil.userIdFromToken(token);

        if (userDataRepository.getUserRoleById(userId) != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to perform this action.");
        }

        boolean isDeleted = reviewRepository.deleteReviewById(reviewId);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found.");
        }
    }

}
