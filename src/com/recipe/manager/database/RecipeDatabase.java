package com.recipe.manager.database;

import com.recipe.manager.model.RecipeModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDatabase {
    public static List<RecipeModel> searchRecipes(String query) throws SQLException {
        String sql = "SELECT * FROM recipes WHERE name LIKE ?";
        List<RecipeModel> recipes = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                recipes.add(new RecipeModel(resultSet.getInt("recipe_id"),
                        resultSet.getString("name"),
                        resultSet.getString("ingredients"),
                        resultSet.getString("instructions")));
            }
        }
        return recipes;
    }

    public static List<RecipeModel> getAllRecipes() throws SQLException {
        String sql = "SELECT * FROM recipes";
        List<RecipeModel> recipes = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                recipes.add(new RecipeModel(resultSet.getInt("recipe_id"),
                        resultSet.getString("name"),
                        resultSet.getString("ingredients"),
                        resultSet.getString("instructions")));
            }
        }
        return recipes;
    }
}
