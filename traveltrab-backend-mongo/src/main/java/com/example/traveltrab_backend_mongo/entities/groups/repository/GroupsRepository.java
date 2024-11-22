package com.example.traveltrab_backend_mongo.entities.groups.repository;


import com.example.traveltrab_backend_mongo.entities.groups.Tasks;
import com.example.traveltrab_backend_mongo.entities.groups.domain.Groups;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupsRepository extends MongoRepository<Groups, String> {
    // Aqui você pode adicionar métodos customizados se necessário.
    // Exemplo: List<Groups> findByNameGroup(String nameGroup);
    Optional<Tasks> findTasksById(String idTasks);
}
