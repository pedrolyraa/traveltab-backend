package com.example.traveltrab_backend_mongo.controllers;

import com.example.traveltrab_backend_mongo.DTOS.GroupsRequestDTO;
import com.example.traveltrab_backend_mongo.DTOS.TasksRequestDTO;
import com.example.traveltrab_backend_mongo.DTOS.UpdateGroupRequestDTO;
import com.example.traveltrab_backend_mongo.entities.groups.Tasks;
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
import java.util.Map;
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
            Groups group = groupsService.createGroup(
                    groupRequestDTO.getNameGroup(),
                    groupRequestDTO.getTypeGroup(),
                    groupRequestDTO.getStartDate(),
                    groupRequestDTO.getEndDate(),
                    groupRequestDTO.getTasks(),
                    groupRequestDTO.getGroupMembers()
            );
            return ResponseEntity.ok(group);
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Groups> updateGroup(@PathVariable String id, @RequestBody UpdateGroupRequestDTO updateGroupRequestDTO) {
        try {
            Groups updatedGroup = groupsService.updateGroup(id, updateGroupRequestDTO);
            return ResponseEntity.ok(updatedGroup);
        } catch  (GroupsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String id) {
        try {
            groupsService.deleteGroup(id);
            return ResponseEntity.noContent().build();
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/addMembers/{id}")
    public ResponseEntity<Groups> addMembers(@PathVariable String id, @RequestBody Set<String> newMembers) {
        try {
            Groups updatedGroup = groupsService.addMembers(id, newMembers);
            return ResponseEntity.ok(updatedGroup);
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Atualização para remoção de membros
    @PutMapping("/removeMember/{groupId}")
    public ResponseEntity<Groups> removeMember(@PathVariable String groupId, @RequestBody Map<String, String> payload) {
        try {
            String userId = payload.get("userId");
            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Verifica se o userId foi fornecido
            }

            Groups updatedGroup = groupsService.removeMember(groupId, userId);
            return ResponseEntity.ok(updatedGroup);
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Groups>> getGroupsByUserId(@PathVariable String userId) {
        try {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new GroupsException("Usuário não encontrado com o ID: " + userId));
            Set<String> groupIds = user.getGroups();
            List<Groups> userGroups = groupsService.findGroupsByIds(groupIds);
            return ResponseEntity.ok(userGroups);
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/byEmail")
    public ResponseEntity<List<Groups>> getGroupsByUserEmail(@RequestParam String email) {
        try {
            Users user = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new GroupsException("Usuário não encontrado com o email: " + email));
            Set<String> groupIds = user.getGroups();
            List<Groups> userGroups = groupsService.findGroupsByIds(groupIds);
            return ResponseEntity.ok(userGroups);
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/detail/{groupId}")
    public ResponseEntity<Groups> getGroupById(@PathVariable String groupId) {
        try {
            Groups group = groupsService.findGroupById(groupId)
                    .orElseThrow(() -> new GroupsException("Grupo com ID " + groupId + " não encontrado."));
            return ResponseEntity.ok(group);
        } catch (GroupsException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
