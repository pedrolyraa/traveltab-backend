package com.example.traveltrab_backend_mongo.controllers;

import com.example.traveltrab_backend_mongo.DTOS.GroupsRequestDTO;
import com.example.traveltrab_backend_mongo.DTOS.UpdateGroupRequestDTO;
import com.example.traveltrab_backend_mongo.entities.groups.domain.Groups;
import com.example.traveltrab_backend_mongo.entities.groups.exception.GroupsException;
import com.example.traveltrab_backend_mongo.entities.groups.service.GroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupsController {

    @Autowired
    private GroupsService groupsService;


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


    // Endpoint para atualizar os dados de um grupo
    @PutMapping ("/update/{id}")
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

}
