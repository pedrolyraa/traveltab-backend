package com.example.traveltrab_backend_mongo.controllers;

import com.example.traveltrab_backend_mongo.entities.users.domain.Users;
import com.example.traveltrab_backend_mongo.entities.users.repository.UsersRepository;
import com.example.traveltrab_backend_mongo.entities.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injeção do PasswordEncoder

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

    // Novo endpoint para buscar o usuário pelo ID
    @GetMapping("/findById/{userId}")
    public ResponseEntity<?> findUserById(@PathVariable String userId) {
        Optional<Users> user = repository.findById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get()); // Retorna o objeto Users encontrado
        } else {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable String userId,
            @RequestBody Map<String, String> updates) {
        try {
            Optional<Users> userOptional = repository.findById(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404).body("Usuário não encontrado");
            }

            Users user = userOptional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "username":
                        user.setUsername(value);
                        break;
                    case "email":
                        user.setEmail(value);
                        break;
                    case "password":
                        user.setPassword(passwordEncoder.encode(value));
                        break;
                    default:
                        throw new IllegalArgumentException("Campo inválido para atualização: " + key);
                }
            });

            repository.save(user);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao atualizar o usuário: " + e.getMessage());
        }
    }

}
