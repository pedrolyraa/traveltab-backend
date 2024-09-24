package com.example.traveltrab_backend_mongo.entities.expenses.repository;

import com.example.traveltrab_backend_mongo.entities.expenses.domain.Expenses;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesRepository extends MongoRepository<Expenses, String> {
    List<Expenses> findByAssignedUsers_UserId(String userId);
}

