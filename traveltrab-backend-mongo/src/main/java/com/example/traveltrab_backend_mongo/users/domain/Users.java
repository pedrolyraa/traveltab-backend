package com.example.traveltrab_backend_mongo.users.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



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

}

