package com.example.traveltrab_backend_mongo.controllers;

import com.example.traveltrab_backend_mongo.DTOS.ExpensesDTO;
import com.example.traveltrab_backend_mongo.entities.expenses.domain.Expenses;
import com.example.traveltrab_backend_mongo.entities.expenses.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Endpoint para marcar uma despesa como paga
//    @PutMapping("/{expenseId}/pay")
//    public ResponseEntity<Expenses> markExpenseAsPaid(@PathVariable String expenseId) {
//        Expenses updatedExpense = expensesService.markExpenseAsPaid(expenseId);
//        return ResponseEntity.ok(updatedExpense);
//    }
//
//    // Endpoint para obter todas as despesas de um usuário
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Expenses>> getUserExpenses(@PathVariable String userId) {
//        List<Expenses> userExpenses = expensesService.getUserExpenses(userId);
//        return ResponseEntity.ok(userExpenses);
//    }
//
//    // Endpoint para adicionar um usuário a uma despesa existente
//    @PostMapping("/{expenseId}/add-user")
//    public ResponseEntity<Expenses> addUserToExpense(
//            @PathVariable String expenseId,
//            @RequestBody AddUserToExpenseDTO addUserDTO) {
//        Expenses updatedExpense = expensesService.addUserToExpense(
//                expenseId,
//                addUserDTO.getUserId(),
//                addUserDTO.getValorInDebt());
//
//        return ResponseEntity.ok(updatedExpense);
//    }

}
