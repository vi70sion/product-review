package lt.productreview.product_review.repository;

import lt.productreview.product_review.model.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ReviewRepository {

    @Value("${database.url}")
    private String url;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    public List<Review> getReviewsByCategory(String category){
        List<Review> reviewList = new ArrayList<>();
        String sql = "SELECT * FROM reviews JOIN categories ON reviews.category_id = categories.id WHERE categories.name = ?;";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {

            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Review review = new Review();
                review.setId(resultSet.getInt("id"));
                review.setUserId(UUID.fromString(resultSet.getString("user_id")));
                review.setCategoryId(resultSet.getInt("category_id"));
                review.setProductName(resultSet.getString("product_name"));
                review.setReviewText(resultSet.getString("review_text"));
                review.setRating(resultSet.getInt("rating"));
                review.setPhoto(resultSet.getBytes("photo"));
                review.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                reviewList.add(review);
            }
            return reviewList;
        } catch (SQLException e) {
            // SQL error
            return null;
        }
    }

}
