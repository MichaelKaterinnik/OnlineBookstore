package com.onlinebookstore.dao;

import com.onlinebookstore.models.EXAMPLE_ApiSection;
import com.onlinebookstore.utils.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * В даному класі регулюється описана реалізація виведення з БД усіх наявних об'єктів типу EXAMPLE_ApiSection, а
 * також додавання у цю таблицю нового об'єкту типу EXAMPLE_ApiSection.
 */
public class EXAMPLE_ApiSectionDAO {
    public List<EXAMPLE_ApiSection> getAllApiSections() {
        List<EXAMPLE_ApiSection> apiSections = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM api_sections";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");

                EXAMPLE_ApiSection apiSection = new EXAMPLE_ApiSection(id, title, description);
                apiSections.add(apiSection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return apiSections;
    }


    public void insertApiSection(EXAMPLE_ApiSection apiSection) {
        //generate this method
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO api_sections (title, description) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, apiSection.getTitle());
            statement.setString(2, apiSection.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
