package lt.productreview.product_review.repository;

import lt.productreview.product_review.model.Role;
import lt.productreview.product_review.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Repository
public class UserDataRepository {

    @Value("${database.url}")
    private String url;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;


    public UUID validateUser(User user) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            ResultSet resultSet = statement.executeQuery();
            return (resultSet.next()) ? UUID.fromString(resultSet.getString("id")): null;
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getId().toString());
            return (statement.executeUpdate() > 0) ? true : false;
        } catch (SQLException e) {
            //
        }
        return false;
    }

    public String getUserNameById(UUID userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {
            statement.setString(1, userId.toString());
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) return "";
            return resultSet.getString("name");
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
        return "";
    }

    public Role getUserRoleById(UUID userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {
            statement.setString(1, userId.toString());
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) return null;
            return Role.valueOf(resultSet.getString("role"));
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
        return null;
    }

}
