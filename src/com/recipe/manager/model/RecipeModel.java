package com.recipe.manager.model;

public class RecipeModel {
    private int recipeId;
    private String name;
    private String ingredients;
    private String instructions;

    public RecipeModel(int recipeId, String name, String ingredients, String instructions) {
        this.recipeId = recipeId;
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }
}
