package com.recipe.manager.ui;

import com.recipe.manager.database.FavoritesDatabase;
import com.recipe.manager.database.RecipeDatabase;
import com.recipe.manager.model.RecipeModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RecipeSearchForm extends JFrame {
    private int userId;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel searchResultsPanel;
    private JButton viewAllRecipesButton;
    private JButton viewFavoritesButton;
    private JButton logoutButton;

    public RecipeSearchForm(int userId) {
        this.userId = userId;
        setTitle("Recipe Manager - Search Recipes");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(240, 248, 255);  // AliceBlue
                Color color2 = new Color(176, 224, 230);  // PowderBlue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        setContentPane(mainPanel);

        // Top panel for search and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        searchField = new JTextField(20);
        searchButton = createStyledButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.WEST);

        // Logout Panel
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);
        logoutButton = createStyledButton("Logout");
        logoutPanel.add(logoutButton);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Search Results Panel
        searchResultsPanel = new JPanel();
        searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.Y_AXIS));
        searchResultsPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(searchResultsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        viewAllRecipesButton = createStyledButton("View All Recipes");
        viewFavoritesButton = createStyledButton("View Favorites");
        buttonsPanel.add(viewAllRecipesButton);
        buttonsPanel.add(viewFavoritesButton);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Event Listeners
        searchButton.addActionListener(e -> searchRecipes());
        viewAllRecipesButton.addActionListener(e -> viewAllRecipes());
        viewFavoritesButton.addActionListener(e -> viewFavorites());
        logoutButton.addActionListener(e -> logout());
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void searchRecipes() {
        String query = searchField.getText();
        try {
            List<RecipeModel> recipes = RecipeDatabase.searchRecipes(query);
            displayRecipes(recipes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Search Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAllRecipes() {
        try {
            List<RecipeModel> recipes = RecipeDatabase.getAllRecipes();
            displayRecipes(recipes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewFavorites() {
        try {
            List<RecipeModel> favorites = FavoritesDatabase.getFavorites(userId);
            displayRecipes(favorites);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayRecipes(List<RecipeModel> recipes) {
        searchResultsPanel.removeAll();
        for (RecipeModel recipe : recipes) {
            JPanel recipePanel = createRecipePanel(recipe);
            searchResultsPanel.add(recipePanel);
            searchResultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        searchResultsPanel.revalidate();
        searchResultsPanel.repaint();
    }

    private JPanel createRecipePanel(RecipeModel recipe) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true));
        panel.setBackground(new Color(240, 248, 255));

        JLabel nameLabel = new JLabel(recipe.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.add(nameLabel, BorderLayout.NORTH);

        JTextArea descriptionArea = new JTextArea(
                "Ingredients: " + recipe.getIngredients() + "\n\n" +
                "Instructions: " + recipe.getInstructions());
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        descriptionArea.setBackground(new Color(240, 248, 255));
        panel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        JButton addFavoriteButton = createStyledButton("Add to Favorites");
        addFavoriteButton.addActionListener(e -> addToFavorites(recipe.getRecipeId()));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addFavoriteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addToFavorites(int recipeId) {
        try {
            FavoritesDatabase.addFavorite(userId, recipeId);
            JOptionPane.showMessageDialog(this, "Recipe added to favorites!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Perform any necessary cleanup
            dispose();  // Close the current window
            new LoginForm().setVisible(true);  // Open the login form
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RecipeSearchForm(1).setVisible(true));
    }
}