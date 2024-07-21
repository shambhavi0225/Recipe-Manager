package com.recipe.manager.database;

import com.recipe.manager.model.RecipeModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoritesDatabase {
    // Add a recipe to the user's favorites
    public static void addFavorite(int userId, int recipeId) throws SQLException {
        String sql = "INSERT INTO favorites (user_id, recipe_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, recipeId);
            statement.executeUpdate();
        }
    }

    // Remove a recipe from the user's favorites
    public static void removeFavorite(int userId, int recipeId) throws SQLException {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND recipe_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, recipeId);
            statement.executeUpdate();
        }
    }

    // Retrieve all favorite recipes for the user
    public static List<RecipeModel> getFavorites(int userId) throws SQLException {
        String sql = "SELECT recipes.* FROM recipes " +
                     "JOIN favorites ON recipes.recipe_id = favorites.recipe_id " +
                     "WHERE favorites.user_id = ?";
        List<RecipeModel> favorites = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                favorites.add(new RecipeModel(resultSet.getInt("recipe_id"),
                        resultSet.getString("name"),
                        resultSet.getString("ingredients"),
                        resultSet.getString("instructions")));
            }
        }
        return favorites;
    }
}
