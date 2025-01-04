package lt.productreview.product_review.repository;

import lt.productreview.product_review.model.EmailForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmailRepository {

    @Value("${database.url}")
    private String url;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    public EmailForm getOneEmail() {
        String sql = "SELECT * FROM newsletters WHERE sent_at IS NULL ORDER BY id ASC LIMIT 1";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = _connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            boolean hasResults = resultSet.next();
            if(!hasResults) return null;
            return new EmailForm(resultSet.getInt("id"),
                    resultSet.getString("content"),
                    null);
        } catch (SQLException e) {
            // sql error
        }
        return null;
    }

    public List<String> getNewsSubscribersEmailsList(){
        String sql = "SELECT * FROM news_subscribers";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {
            List<String> newsSubscribers = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                newsSubscribers.add(resultSet.getString("email"));
            }
            return newsSubscribers;
        } catch (SQLException e) {
            // SQL išimtis
            return null;
        }
    }

    public boolean updateEmail(EmailForm emailForm) {
        String sql = "UPDATE newsletters SET sent_at = ? WHERE id = ?";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(2, emailForm.getId());
            return (statement.executeUpdate() > 0) ? true : false;
        } catch (SQLException e) {
            //
        }
        return false;
    }

}
