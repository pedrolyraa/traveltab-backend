package com.example.traveltrab_backend_mongo.entities.expenses.exceptions;

public class ExpensesExceptions extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExpensesExceptions(String message) {
        super(message);
    }

    public ExpensesExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}

