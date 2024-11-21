package com.example.traveltrab_backend_mongo.controllers;

import com.example.traveltrab_backend_mongo.entities.users.domain.Users;
import com.example.traveltrab_backend_mongo.entities.users.repository.UsersRepository;
import com.example.traveltrab_backend_mongo.entities.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private UserService userService;

    @GetMapping("/totalDebt")
    public ResponseEntity<Float> getTotalDebt() {
        Float totalDebt = userService.calculateTotalDebt();
        return ResponseEntity.ok(totalDebt);
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<?> findUserByEmail(@RequestParam String email) {
        Optional<Users> user = repository.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }

    // Exemplo de outro endpoint para enviar uma solicitação de amizade
    @PostMapping("/add")
    public ResponseEntity<String> sendFriendRequest(@RequestParam String targetEmail) {
        String result = userService.sendFriendRequest(targetEmail);
        return ResponseEntity.ok(result);
    }

    // Endpoint simples para testar a conexão do controller
    @GetMapping
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok("Sucesso");
    }
}
