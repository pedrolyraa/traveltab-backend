package com.example.traveltrab_backend_mongo.entities.expenses.service;

import com.example.traveltrab_backend_mongo.DTOS.UpdateExpensesRequestDTO;
import com.example.traveltrab_backend_mongo.entities.expenses.domain.AssignedUserDebt;
import com.example.traveltrab_backend_mongo.entities.expenses.domain.Expenses;
import com.example.traveltrab_backend_mongo.entities.expenses.repository.ExpensesRepository;
import com.example.traveltrab_backend_mongo.entities.groups.domain.Groups;
import com.example.traveltrab_backend_mongo.entities.groups.repository.GroupsRepository;
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

    @Autowired
    private GroupsRepository groupsRepository;

    public Expenses createExpense(String description, Float balance, Map<String, Float> assignedUsersMap, Set<String> assignedGroups, boolean isSplitEvenly) {
        // Verificar se assignedGroups está nulo
        if (assignedGroups == null || assignedGroups.isEmpty()) {
            throw new RuntimeException("Assigned groups cannot be null or empty");
        }

        // Criação de uma nova despesa
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

        Expenses savedExpense = expensesRepository.save(newExpense);

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

        for (String groupId : assignedGroups) {
            Groups group = groupsRepository.findById(groupId)
                    .orElseThrow(() -> new RuntimeException("Grupo não encontrado com ID: " + groupId));

            List<Expenses> expenses = group.getExpenses();
            if (expenses == null) {
                expenses = new ArrayList<>();
            }
            expenses.add(savedExpense); // Adiciona a despesa ao grupo
            group.setExpenses(expenses);

            // Salvar as alterações no grupo
            groupsRepository.save(group);
        }

        return newExpense;
    }

    public void deleteExpense(String expenseId) {
        // Buscar a despesa no banco de dados
        Expenses expense = expensesRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada com ID: " + expenseId));

        // Remover a despesa dos grupos associados
        for (String groupId : expense.getAssignedGroups()) {
            Groups group = groupsRepository.findById(groupId)
                    .orElseThrow(() -> new RuntimeException("Grupo não encontrado com ID: " + groupId));

            group.getExpenses().removeIf(exp -> exp.getId().equals(expenseId));
            groupsRepository.save(group); // Atualiza o grupo após remover a despesa
        }

        // Remover a despesa dos usuários associados
        for (AssignedUserDebt userDebt : expense.getAssignedUsers()) {
            String userId = userDebt.getUserId();
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + userId));

            Map<String, Float> currentDebt = user.getCurrentDebt();
            if (currentDebt != null) {
                currentDebt.remove(expenseId);
                user.setCurrentDebt(currentDebt);
                usersRepository.save(user); // Atualiza o usuário após remover a despesa
            }
        }

        // Finalmente, remover a despesa do banco de dados
        expensesRepository.deleteById(expenseId);
    }

    public Expenses updateExpense(String expenseId, UpdateExpensesRequestDTO updateExpenseRequestDTO, Map<String, Float> newAssignedUsersMap, boolean isSplitEvenly) {
        // Buscar a despesa no banco de dados
        Expenses expense = expensesRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada com ID: " + expenseId));

        // Atualizar a descrição, se fornecida
        if (updateExpenseRequestDTO.getDescription() != null) {
            expense.setDescription(updateExpenseRequestDTO.getDescription());
        }

        // Atualizar o valor total da despesa, se fornecido
        if (updateExpenseRequestDTO.getBalance() != null) {
            expense.setBalance(updateExpenseRequestDTO.getBalance());
        }

        // Atualizar o status de pagamento, se fornecido
        if (updateExpenseRequestDTO.getIsPaid() != null) {
            expense.setPaid(updateExpenseRequestDTO.getIsPaid());
        }

        // Validar o mapa de usuários atribuídos
        if (newAssignedUsersMap != null && !newAssignedUsersMap.isEmpty()) {
            List<AssignedUserDebt> updatedAssignedUsers = new ArrayList<>();
            if (isSplitEvenly && expense.getBalance() != null) {
                Float splitAmount = expense.getBalance() / newAssignedUsersMap.size();

                for (String userId : newAssignedUsersMap.keySet()) {
                    updatedAssignedUsers.add(new AssignedUserDebt(userId, splitAmount));
                }
            } else {
                for (Map.Entry<String, Float> entry : newAssignedUsersMap.entrySet()) {
                    updatedAssignedUsers.add(new AssignedUserDebt(entry.getKey(), entry.getValue()));
                }
            }
            expense.setAssignedUsers(updatedAssignedUsers);
        }

        // Persistir as alterações na coleção `expenses`
        Expenses updatedExpense = expensesRepository.save(expense);

        // Atualizar a despesa na coleção `groups`
        for (String groupId : expense.getAssignedGroups()) {
            Groups group = groupsRepository.findById(groupId)
                    .orElseThrow(() -> new RuntimeException("Grupo não encontrado com ID: " + groupId));

            // Atualizar a despesa correspondente no grupo
            group.getExpenses().stream()
                    .filter(groupExpense -> groupExpense.getId().equals(expenseId))
                    .forEach(groupExpense -> {
                        groupExpense.setDescription(updatedExpense.getDescription());
                        groupExpense.setBalance(updatedExpense.getBalance());
                        groupExpense.setAssignedUsers(updatedExpense.getAssignedUsers());
                        groupExpense.setPaid(updatedExpense.isPaid());
                    });

            // Salvar as alterações no grupo
            groupsRepository.save(group);
        }

        return updatedExpense;
    }




}
