package com.recipe.manager.model;

public class UserModel {
    private int userId;
    private String username;
    private String password;

    public UserModel(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
