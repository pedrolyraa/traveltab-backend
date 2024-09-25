package com.example.traveltrab_backend_mongo.entities.groups.domain;

import com.example.traveltrab_backend_mongo.entities.expenses.domain.Expenses;
import com.example.traveltrab_backend_mongo.entities.groups.enums.TypeGroup;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
@EqualsAndHashCode(of = "id")
public class Groups {
    private String id;
    private Set<String> groupMembers;  // Conjunto de IDs dos usuários
    private String nameGroup;
    private TypeGroup typeGroup;       // Usando o Enum TypeGroup
    private Date startDate;
    private Date endDate;
    private List<Expenses> expenses;
}
