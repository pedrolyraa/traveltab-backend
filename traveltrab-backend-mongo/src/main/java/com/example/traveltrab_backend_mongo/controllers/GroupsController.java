package com.example.traveltrab_backend_mongo.controllers;

import com.example.traveltrab_backend_mongo.DTOS.GroupsRequestDTO;
import com.example.traveltrab_backend_mongo.DTOS.UpdateGroupRequestDTO;
import com.example.traveltrab_backend_mongo.entities.groups.domain.Groups;
import com.example.traveltrab_backend_mongo.entities.groups.exception.GroupsException;
import com.example.traveltrab_backend_mongo.entities.groups.service.GroupsService;
import com.example.traveltrab_backend_mongo.entities.users.domain.Users;
import com.example.traveltrab_backend_mongo.entities.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupsController {

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("/create")
    public ResponseEntity<Groups> createGroup(@RequestBody GroupsRequestDTO groupRequestDTO) {
        try {
            // Chamar o serviço com os dados do DTO
            Groups group = groupsService.createGroup(
                    groupRequestDTO.getNameGroup(),
                    groupRequestDTO.getTypeGroup(),
                    groupRequestDTO.getStartDate(),
                    groupRequestDTO.getEndDate(),
                    groupRequestDTO.getGroupMembers()
            );
            return ResponseEntity.ok(group);
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);  // Envia erro 400 caso haja falha na criação
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Groups> updateGroup(
            @PathVariable String id,
            @RequestBody UpdateGroupRequestDTO updateGroupRequestDTO) {
        try {
            Groups updatedGroup = groupsService.updateGroup(id, updateGroupRequestDTO);
            return ResponseEntity.ok(updatedGroup);
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String id) {
        try {
            groupsService.deleteGroup(id);
            return ResponseEntity.noContent().build();  // Retorna 204 No Content em caso de sucesso
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);  // Retorna 400 Bad Request em caso de erro
        }
    }

    @PutMapping("/addMembers/{id}")
    public ResponseEntity<Groups> addMembers(@PathVariable String id, @RequestBody Set<String> newMembers) {
        try {
            Groups updatedGroup = groupsService.addMembers(id, newMembers);
            return ResponseEntity.ok(updatedGroup);
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);  // Retorna 400 em caso de erro
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Groups>> getGroupsByUserId(@PathVariable String userId) {
        try {
            // Verificar se o usuário existe
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new GroupsException("Usuário não encontrado com o ID: " + userId));

            // Obter os IDs dos grupos associados ao usuário
            Set<String> groupIds = user.getGroups();

            // Obter os grupos correspondentes usando os IDs
            List<Groups> userGroups = groupsService.findGroupsByIds(groupIds);

            return ResponseEntity.ok(userGroups);
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/byEmail")
    public ResponseEntity<List<Groups>> getGroupsByUserEmail(@RequestParam String email) {
        try {
            // Verificar se o usuário existe com base no email
            Users user = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new GroupsException("Usuário não encontrado com o email: " + email));

            // Obter os IDs dos grupos associados ao usuário
            Set<String> groupIds = user.getGroups();

            // Adicionar log para verificar os IDs dos grupos
            System.out.println("Group IDs encontrados para o usuário: " + groupIds);

            // Obter os grupos correspondentes usando os IDs
            List<Groups> userGroups = groupsService.findGroupsByIds(groupIds);

            // Adicionar log para verificar se os grupos foram encontrados
            System.out.println("Grupos encontrados: " + userGroups);

            return ResponseEntity.ok(userGroups);
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
