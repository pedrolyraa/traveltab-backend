package com.example.traveltrab_backend_mongo.entities.users.domain;


import com.example.traveltrab_backend_mongo.DTOS.UsersDTO;
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
public class Users  {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private Set<String> friendList;
    private Set<String> friendRequests;
    private Map<String, Float> currentDebt;  // Mapeia ID do usuário para o valor da dívida (dever)
    private Map<String, Float> debtOwedToMe;  // Mapeia ID do usuário para o valor que eles me devem (a receber)
    private Set<String> groups;

}

