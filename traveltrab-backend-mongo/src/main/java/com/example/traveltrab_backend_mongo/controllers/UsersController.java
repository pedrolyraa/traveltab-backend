package com.example.traveltrab_backend_mongo.controllers;


import com.example.traveltrab_backend_mongo.entities.users.repository.UsersRepository;
import com.example.traveltrab_backend_mongo.entities.users.UsersService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<String> sendFriendRequest(@RequestParam String targetEmail) {
        String result = userService.sendFriendRequest(targetEmail);
        return ResponseEntity.ok(result);
    }
    //      Users
    //    String id;
    //    String username;
    //    String email;
    //    String password;
    //    Set<String> friendList;
    //    Set<String> friendRequests;
    //    Map<String, Float> currentDebt;
    //    Map<String, Float> debtOwedToMe;
    //
    // Grupo
    //  id String
    //  Set<String> groupMembers;
    //  String nameGroup;
    //  String typeGroup;
    //  Date startDate
    //  Date endDate;
    //  List<Expenses> expenses;
    //
    //
    // Expenses
    //  id String
    //  description String
    //  balance Float
    //  List<AssignedUserDebt> assignedUsers;
    //  assignedGroups [SetGroups]
    //  isPaid boolean

//    public class AssignedUserDebt {
//        String userId;  // ID do usuário
//        Float valorInDebt;  // Valor que o usuário está devendo
//    }


    @GetMapping
    public ResponseEntity getUser(){
        return ResponseEntity.ok( "Sucesso");
    }


}
