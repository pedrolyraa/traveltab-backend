package com.example.traveltrab_backend_mongo.controllers;

import com.example.traveltrab_backend_mongo.DTOS.ExpensesDTO;
import com.example.traveltrab_backend_mongo.DTOS.UpdateExpensesRequestDTO;
import com.example.traveltrab_backend_mongo.entities.expenses.UpdateExpenseRequestPayload;
import com.example.traveltrab_backend_mongo.entities.expenses.domain.Expenses;
import com.example.traveltrab_backend_mongo.entities.expenses.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> updateExpense(@PathVariable String id, @RequestBody UpdateExpenseRequestPayload payload) {
        try {
            Expenses updatedExpense = expensesService.updateExpense(
                    id,
                    payload.getUpdateExpensesRequestDTO(),
                    payload.getNewAssignedUsersMap(),
                    payload.isSplitEvenly()
            );
            return ResponseEntity.ok(updatedExpense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar a despesa: " + e.getMessage());
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

    @PutMapping("/markPaid/{expenseId}/{userId}")
    public ResponseEntity<?> markAsPaid(
            @PathVariable String expenseId,
            @PathVariable String userId,
            @RequestBody Map<String, Boolean> payload
    ) {
        boolean isPaid = payload.get("isPaid"); // Lê o valor enviado
        try {
            Expenses updatedExpense = expensesService.updateUserPaymentStatus(expenseId, userId, isPaid);
            return ResponseEntity.ok(updatedExpense);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable String id) {
        try {
            Expenses expense = expensesService.getExpenseById(id);
            return ResponseEntity.ok(expense);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getExpensesByUserId(@PathVariable String userId) {
        try {
            List<Expenses> userExpenses = expensesService.getExpensesByUserId(userId);
            return ResponseEntity.ok(userExpenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao buscar despesas: " + e.getMessage());
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
