package com.example.traveltrab_backend_mongo.entities.expenses;

import com.example.traveltrab_backend_mongo.DTOS.UpdateExpensesRequestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;



@Getter
@Setter
public class UpdateExpenseRequestPayload {
    private UpdateExpensesRequestDTO updateExpensesRequestDTO; // Dados gerais da despesa
    private Map<String, Float> newAssignedUsersMap; // Novo mapa de valores por usuário
    private boolean splitEvenly; // Indica se deve dividir igualmente

    public void setUpdateExpensesRequestDTO(UpdateExpensesRequestDTO updateExpensesRequestDTO) {
        this.updateExpensesRequestDTO = updateExpensesRequestDTO;
    }

    public void setNewAssignedUsersMap(Map<String, Float> newAssignedUsersMap) {
        this.newAssignedUsersMap = newAssignedUsersMap;
    }

    public void setSplitEvenly(boolean splitEvenly) {
        this.splitEvenly = splitEvenly;
    }
}
