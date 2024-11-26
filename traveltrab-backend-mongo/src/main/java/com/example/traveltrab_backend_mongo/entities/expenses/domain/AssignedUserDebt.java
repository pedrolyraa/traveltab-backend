package com.example.traveltrab_backend_mongo.entities.expenses.domain;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.util.Assert;

public class AssignedUserDebt {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("valorInDebt")
    private Float valorInDebt;
    @JsonProperty("isPaid")
    private boolean isPaid;

    // Construtor padrão necessário para deserialização
    public AssignedUserDebt() {
        // Inicializações padrão, se necessário
        this.isPaid = false;
    }

    // Construtor completo
    public AssignedUserDebt(String userId, Float valorInDebt, boolean isPaid) {
        Assert.notNull(userId, "UserId must not be null");
        Assert.notNull(valorInDebt, "ValorInDebt must not be null");
        this.userId = userId;
        this.valorInDebt = valorInDebt;
        this.isPaid = isPaid;
    }

    // Getters e setters
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

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "AssignedUserDebt{" +
                "userId='" + userId + '\'' +
                ", valorInDebt=" + valorInDebt +
                ", isPaid=" + isPaid +
                '}';
    }
}
