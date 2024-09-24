package com.example.traveltrab_backend_mongo.DTOS;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.catalina.User;

import java.util.List;

@Data
@AllArgsConstructor
public class UsersDTO {
    private String id;
    private String name;
    private String email;
}
