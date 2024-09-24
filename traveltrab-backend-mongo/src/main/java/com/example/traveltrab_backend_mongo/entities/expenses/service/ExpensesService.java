package com.example.traveltrab_backend_mongo.entities.expenses.service;


import com.example.traveltrab_backend_mongo.entities.expenses.domain.AssignedUserDebt;
import com.example.traveltrab_backend_mongo.entities.expenses.domain.Expenses;
import com.example.traveltrab_backend_mongo.entities.expenses.exceptions.ExpensesExceptions;
import com.example.traveltrab_backend_mongo.entities.expenses.repository.ExpensesRepository;
import com.example.traveltrab_backend_mongo.entities.users.domain.Users;
import com.example.traveltrab_backend_mongo.entities.users.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpensesService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ExpensesRepository expensesRepository;

    public Expenses createExpense(String description, Float balance, Map<String, Float> assignedUsersMap, Set<String> assignedGroups, boolean isSplitEvenly) {
        // Criar nova despesa
        Expenses newExpense = new Expenses();
        newExpense.setDescription(description);
        newExpense.setBalance(balance);
        newExpense.setAssignedGroups(assignedGroups);
        newExpense.setPaid(false);

        List<AssignedUserDebt> assignedUsers = new ArrayList<>(); // Usando List para AssignedUserDebt

        if (isSplitEvenly) {
            // Dividir igualmente entre os usuários
            Float splitAmount = balance / assignedUsersMap.size();

            for (String userId : assignedUsersMap.keySet()) {
                assignedUsers.add(new AssignedUserDebt(userId, splitAmount));
            }
        } else {
            // Atualizar a lista com valores específicos fornecidos
            for (Map.Entry<String, Float> entry : assignedUsersMap.entrySet()) {
                String userId = entry.getKey();
                Float debtAmount = entry.getValue();
                assignedUsers.add(new AssignedUserDebt(userId, debtAmount));
            }
        }

        // Setar a lista de assignedUsers
        newExpense.setAssignedUsers(assignedUsers);

        // Salvar a despesa no banco
        expensesRepository.save(newExpense);

        // Atualizar o currentDebt dos usuários envolvidos
        for (AssignedUserDebt userDebt : assignedUsers) {
            String userId = userDebt.getUserId();
            Float debtValue = userDebt.getValorInDebt();
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            // Atualizar currentDebt do usuário
            Map<String, Float> currentDebt = user.getCurrentDebt();
            if (currentDebt == null) {
                currentDebt = new HashMap<>();
            }
            currentDebt.put(newExpense.getId(), debtValue); // Adiciona a dívida da nova despesa
            user.setCurrentDebt(currentDebt);

            // Salvar as alterações no usuário
            usersRepository.save(user);
        }

        return newExpense;
    }



//    public Expenses markExpenseAsPaid(String expenseId) {
//        Expenses expense = expensesRepository.findById(expenseId)
//                .orElseThrow(() -> new ExpensesExceptions("Dispesa não encontrada"));
//        expense.setPaid(true);
//
//        // Atualiza os saldos dos usuários
//        for (AssignedUserDebt userDebt : expense.getAssignedUsers()) {
//            Users user = usersRepository.findById(userDebt.getUserId())
//                    .orElseThrow(() -> new ExpensesExceptions("Usuário não encontrado"));
//
//            // Remove a dívida dos registros do usuário
//            Map<String, Float> currentDebt = user.getCurrentDebt();
//            if (currentDebt != null) {
//                currentDebt.remove(userDebt.getUserId());
//            }
//
//            // Atualiza o usuário no repositório
//            usersRepository.save(user);
//        }
//
//        // Salva a despesa como paga no repositório
//        return expensesRepository.save(expense);
//    }



}
