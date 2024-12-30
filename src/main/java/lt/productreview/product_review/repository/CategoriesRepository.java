package lt.productreview.product_review.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoriesRepository {

    @Value("${database.url}")
    private String url;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    public List<String> allCategoriesList(){
        List<String> categoriesList = new ArrayList<>();
        String sql = "SELECT name FROM categories";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                categoriesList.add(resultSet.getString("name"));
            }
            return categoriesList;
        } catch (SQLException e) {
            // SQL error
            return null;
        }
    }

    public int getCategoryIdFromName(String category){
        String sql = "SELECT id FROM categories WHERE name = ?;";
        try (Connection _connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = _connection.prepareStatement(sql)) {
            int categoryId =0;
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) categoryId = resultSet.getInt("id");
            return categoryId;
        } catch (SQLException e) {
            // SQL error
            return 0;
        }
    }

}
