package lt.productreview.product_review.repository;

import lt.productreview.product_review.model.Review;
import lt.productreview.product_review.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.format.DateTimeFormatter;
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

    public List<Review> getReviewsByUserId(UUID userId, Role userRole){
        List<Review> reviewList = new ArrayList<>();
        String sql;
        if (userRole == Role.ADMIN) {
            sql = "SELECT * FROM reviews";
        } else {
            sql = "SELECT * FROM reviews JOIN users ON reviews.user_id = users.id WHERE users.id = ?";
        }

        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {

            if (userRole == Role.USER) statement.setString(1, userId.toString());
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

    public boolean addReview(Review review) {
        String sql = "INSERT INTO reviews ( user_id, category_id, product_name, review_text, photo, rating, created_at) VALUES (?,?,?,?,?,?,?)";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {

            statement.setString(1, review.getUserId().toString());
            statement.setInt(2, review.getCategoryId());
            statement.setString(3, review.getProductName());
            statement.setString(4, review.getReviewText());
            statement.setBytes(5, review.getPhoto());
            statement.setInt(6, review.getRating());
            statement.setString(7, review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) return true;
        } catch (SQLException e) {
            //throw new RuntimeException(e);
        }
        return false;
    }

    public boolean deleteReviewById(int reviewId) {
        String sql = "DELETE FROM reviews WHERE id = ?";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {
            statement.setInt(1, reviewId);
            int rowsDeleted = statement.executeUpdate();
            if(rowsDeleted > 0) return true;
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
        return false;
    }

    public List<Review> getReviewsBySearchText(String searchText){
        List<Review> reviewList = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE product_name LIKE ?";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {

            statement.setString(1, "%" + searchText + "%");
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
