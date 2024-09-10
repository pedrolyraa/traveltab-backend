package com.example.traveltrab_backend_mongo.controllers;


import com.example.traveltrab_backend_mongo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersRepository repository;

//    @GetMapping
//    public ResponseEntity getAllUsers(){
//        var allUsers = repository.findAll();
//        return ResponseEntity.ok(allUsers);
//    }

    @GetMapping
    public ResponseEntity getUser(){
        return ResponseEntity.ok( "Sucesso");
    }


}
