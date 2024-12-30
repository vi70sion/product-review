package lt.productreview.product_review.service;

import lt.productreview.product_review.model.Review;
import lt.productreview.product_review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    public List<Review> getReviewsByCategory(String category) {
        return reviewRepository.getReviewsByCategory(category);
    }

    public boolean addReview(Review review) {
        return reviewRepository.addReview(review);
    }

}
