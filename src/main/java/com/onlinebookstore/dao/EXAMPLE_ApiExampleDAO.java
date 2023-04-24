package com.onlinebookstore.dao;

import com.onlinebookstore.models.EXAMPLE_ApiExample;
import com.onlinebookstore.utils.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * В даному класі регулюється описана логіка класу типу DAO, в якому реалізовано метод вилучення з БД
 * екземпляру відповідного об'єкту-моделі за значенням. що міститься в одній із колонок таблиці. Результатом
 * може бути декілька об'єктів, а тому повертаємо список.
 */
public class EXAMPLE_ApiExampleDAO {
    public List<EXAMPLE_ApiExample> getApiExamplesBySectionId(int sectionId) {
        List<EXAMPLE_ApiExample> apiExamples = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM api_examples WHERE section_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sectionId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String codeSnippet = resultSet.getString("code_snippet");

                EXAMPLE_ApiExample apiExample = new EXAMPLE_ApiExample(id, sectionId, title, codeSnippet);
                apiExamples.add(apiExample);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return apiExamples;
    }

    public void insertApiExample(EXAMPLE_ApiExample apiExample) {
    }
}
