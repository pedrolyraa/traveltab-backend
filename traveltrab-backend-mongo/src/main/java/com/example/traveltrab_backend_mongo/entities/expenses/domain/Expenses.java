package com.example.traveltrab_backend_mongo.entities.expenses.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
@EqualsAndHashCode(of = "id")
public class Expenses {
    @Id
    private String id;
    private String description;
    private Float balance;  // Valor total da despesa
    private List<AssignedUserDebt> assignedUsers;  // Usuários e suas dívidas associadas a essa despesa
    private Set<String> assignedGroups;  // Grupos associados a essa despesa
    private boolean isPaid;  // Indica se a despesa foi paga
}

