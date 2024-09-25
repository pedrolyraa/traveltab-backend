package com.example.traveltrab_backend_mongo.controllers;

import com.example.traveltrab_backend_mongo.DTOS.ExpensesDTO;
import com.example.traveltrab_backend_mongo.DTOS.UpdateExpensesRequestDTO;
import com.example.traveltrab_backend_mongo.entities.expenses.domain.Expenses;
import com.example.traveltrab_backend_mongo.entities.expenses.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpensesController {

    @Autowired
    private ExpensesService expensesService;
    @PostMapping("/create")
    public ResponseEntity<Expenses> createExpense(
            @RequestBody ExpensesDTO expenseDTO) {
        // Criação de uma nova despesa
        Expenses expense = expensesService.createExpense(
                expenseDTO.getDescription(),
                expenseDTO.getBalance(),
                expenseDTO.getAssignedUsers(),
                expenseDTO.getAssignedGroups(),
                expenseDTO.isSplitEvenly());

        return ResponseEntity.ok(expense);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Expenses> updateExpense(
            @PathVariable String id,
            @RequestBody UpdateExpensesRequestDTO updateExpensesRequestDTO) {
        try {
            Expenses updatedExpense = expensesService.updateExpense(id, updateExpensesRequestDTO);
            return ResponseEntity.ok(updatedExpense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String id) {
        try {
            expensesService.deleteExpense(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


//    @PostMapping("/add-members/{id}")
//    public ResponseEntity<Expenses> addMembersToExpense(
//            @PathVariable String id,
//            @RequestBody Map<String, Float> newAssignedUsersMap) {
//        try {
//            Expenses updatedExpense = expensesService.addMembersToExpense(id, newAssignedUsersMap);
//            return ResponseEntity.ok(updatedExpense);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }

}
