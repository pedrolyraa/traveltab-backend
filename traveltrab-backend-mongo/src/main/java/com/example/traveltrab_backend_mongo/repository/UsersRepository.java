package com.example.traveltrab_backend_mongo.repository;

import com.example.traveltrab_backend_mongo.users.domain.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


// <Entidade, Tipo do ID>
public interface UsersRepository extends MongoRepository<Users, String> {

    //Consultar Login
    Optional<Users> findByEmail(String email);
}
