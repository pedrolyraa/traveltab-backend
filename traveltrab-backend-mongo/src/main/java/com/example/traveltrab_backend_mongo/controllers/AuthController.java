package com.example.traveltrab_backend_mongo.controllers;

import com.example.traveltrab_backend_mongo.DTOS.LoginRequestDTO;
import com.example.traveltrab_backend_mongo.DTOS.RegisterDTO;
import com.example.traveltrab_backend_mongo.DTOS.TokenResponseDTO;
import com.example.traveltrab_backend_mongo.infra.security.TokenService;
import com.example.traveltrab_backend_mongo.entities.users.repository.UsersRepository;
import com.example.traveltrab_backend_mongo.entities.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsersRepository repository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        Users user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new TokenResponseDTO(user.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO body){
        Optional<Users> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()) {
            Users newUser = new Users();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setUsername(body.username());

            newUser.setFriendList(new HashSet<>());
            newUser.setFriendRequests(new HashSet<>());

            newUser.setCurrentDebt(new HashMap<>());
            newUser.setDebtOwedToMe(new HashMap<>());
            newUser.setGroups(new HashSet<>());
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new TokenResponseDTO(newUser.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
