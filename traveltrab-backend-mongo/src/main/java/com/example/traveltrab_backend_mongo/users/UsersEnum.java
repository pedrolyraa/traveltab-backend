package com.example.traveltrab_backend_mongo.users;

public enum UsersEnum {

    ADMIN("admin"),
    USER("user");

    private String role;

    // Correção do construtor
    private UsersEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
