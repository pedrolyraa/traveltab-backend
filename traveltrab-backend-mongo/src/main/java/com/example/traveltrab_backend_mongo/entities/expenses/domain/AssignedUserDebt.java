package com.example.traveltrab_backend_mongo.entities.expenses.domain;

public class AssignedUserDebt {
    private String userId;  // ID do usuário
    private Float valorInDebt;  // Valor que o usuário está devendo

    // Getters e Setters

    public AssignedUserDebt(String userId, Float valorInDebt) {
        this.userId = userId;
        this.valorInDebt = valorInDebt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Float getValorInDebt() {
        return valorInDebt;
    }

    public void setValorInDebt(Float valorInDebt) {
        this.valorInDebt = valorInDebt;
    }
}

