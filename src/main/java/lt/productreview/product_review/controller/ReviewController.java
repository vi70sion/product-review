package lt.productreview.product_review.controller;

import lt.productreview.product_review.service.AuthorizationService;
import lt.productreview.product_review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AuthorizationService authorizationService;


    @GetMapping("/category")
    public ResponseEntity<?> getReviewsByCategory(@RequestParam String category,
                                                  @RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<String> validationResponse = authorizationService.validateAuthorizationHeader(authorizationHeader);
        if (validationResponse != null) {
            return validationResponse;
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reviewService.getReviewsByCategory(category));
    }


    @GetMapping("/user")
    public ResponseEntity<?> getReviewsByUserId(@RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<String> validationResponse = authorizationService.validateAuthorizationHeader(authorizationHeader);
        if (validationResponse != null) {
            return validationResponse;
        }
        return reviewService.getReviewsByUserId(authorizationHeader);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestPart("review") String reviewJson,
                                       @RequestParam("image") MultipartFile image,
                                       @RequestParam("category") String category,
                                       @RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<String> validationResponse = authorizationService.validateAuthorizationHeader(authorizationHeader);
        if (validationResponse != null) {
            return validationResponse;
        }
        boolean addSuccess = reviewService.addReview(reviewJson, image, category, authorizationHeader);
        if (addSuccess) return ResponseEntity.status(HttpStatus.OK).body("success");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the review");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReviewById(@RequestParam("reviewId") int reviewId,
                                                   @RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<String> validationResponse = authorizationService.validateAuthorizationHeader(authorizationHeader);
        if (validationResponse != null) {
            return validationResponse;
        }
        return reviewService.deleteReviewById(reviewId, authorizationHeader);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getReviewsBySearchText(@RequestParam("searchtext") String searchText,
                                                  @RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<String> validationResponse = authorizationService.validateAuthorizationHeader(authorizationHeader);
        if (validationResponse != null) {
            return validationResponse;
        }
        return reviewService.getReviewsBySearchText(searchText, authorizationHeader);
    }

}
