package com.example.traveltrab_backend_mongo.entities.groups.service;

import com.example.traveltrab_backend_mongo.DTOS.GroupsRequestDTO;
import com.example.traveltrab_backend_mongo.DTOS.UpdateGroupRequestDTO;
import com.example.traveltrab_backend_mongo.entities.expenses.domain.Expenses;
import com.example.traveltrab_backend_mongo.entities.groups.domain.Groups;
import com.example.traveltrab_backend_mongo.entities.groups.enums.TypeGroup;
import com.example.traveltrab_backend_mongo.entities.groups.exception.GroupsException;
import com.example.traveltrab_backend_mongo.entities.groups.repository.GroupsRepository;
import com.example.traveltrab_backend_mongo.entities.users.domain.Users;
import com.example.traveltrab_backend_mongo.entities.users.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupsService {

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Groups createGroup(String nameGroup, TypeGroup typeGroup, Date startDate, Date endDate, Set<String> groupMembers) {
        if (typeGroup == TypeGroup.VIAGEM) {
            if (startDate == null || endDate == null) {
                throw new GroupsException("Grupos do tipo 'VIAGEM' devem conter Datas de começo e fim.");
            }
        }

        Groups newGroup = new Groups();
        newGroup.setNameGroup(nameGroup);
        newGroup.setTypeGroup(typeGroup);
        newGroup.setStartDate(startDate);
        newGroup.setEndDate(endDate);
        newGroup.setGroupMembers(groupMembers);
        newGroup.setExpenses(new ArrayList<>());

        Groups savedGroup = groupsRepository.save(newGroup);
        System.out.println("Grupo criado com ID: " + savedGroup.getId());

        for (String userId : groupMembers) {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new GroupsException("Usuário com ID " + userId + " não encontrado."));

            Set<String> userGroups = user.getGroups();
            if (userGroups == null) {
                userGroups = new HashSet<>();
            }

            userGroups.add(savedGroup.getId());
            user.setGroups(userGroups);

            usersRepository.save(user);
            System.out.println("Usuário atualizado com novos grupos: " + user.getGroups());
        }

        return savedGroup;
    }

    public void deleteGroup(String id) {
        Groups group = groupsRepository.findById(id)
                .orElseThrow(() -> new GroupsException("Grupo com ID " + id + " não encontrado."));

        Set<String> groupMembers = group.getGroupMembers();
        for (String userId : groupMembers) {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new GroupsException("Usuário com ID " + userId + " não encontrado."));

            Set<String> userGroups = user.getGroups();
            if (userGroups != null) {
                userGroups.remove(group.getId());
                user.setGroups(userGroups);

                usersRepository.save(user);
            }
        }

        groupsRepository.delete(group);
    }

    public Groups updateGroup(String id, UpdateGroupRequestDTO updateGroupRequestDTO) {
        Groups group = groupsRepository.findById(id)
                .orElseThrow(() -> new GroupsException("Grupo com ID " + id + " não encontrado."));

        if (updateGroupRequestDTO.getNameGroup() != null) {
            group.setNameGroup(updateGroupRequestDTO.getNameGroup());
        }

        if (updateGroupRequestDTO.getTypeGroup() != null) {
            if (updateGroupRequestDTO.getTypeGroup() == TypeGroup.VIAGEM) {
                if (updateGroupRequestDTO.getStartDate() == null || updateGroupRequestDTO.getEndDate() == null) {
                    throw new GroupsException("Grupos do tipo 'VIAGEM' devem conter Datas de começo e fim.");
                }
            }
            group.setTypeGroup(updateGroupRequestDTO.getTypeGroup());
        }

        if (updateGroupRequestDTO.getStartDate() != null) {
            group.setStartDate(updateGroupRequestDTO.getStartDate());
        }

        if (updateGroupRequestDTO.getEndDate() != null) {
            group.setEndDate(updateGroupRequestDTO.getEndDate());
        }

        return groupsRepository.save(group);
    }

    public Groups addMembers(String groupId, Set<String> newMembers) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new GroupsException("Grupo com ID " + groupId + " não encontrado."));

        Set<String> membersSet = new HashSet<>(newMembers);
        group.getGroupMembers().addAll(membersSet);

        Groups updatedGroup = groupsRepository.save(group);

        for (String userId : membersSet) {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new GroupsException("Usuário com ID " + userId + " não encontrado."));

            Set<String> userGroups = user.getGroups();
            if (userGroups == null) {
                userGroups = new HashSet<>();
            }

            userGroups.add(updatedGroup.getId());
            user.setGroups(userGroups);

            usersRepository.save(user);
            System.out.println("Usuário atualizado com novos grupos: " + user.getGroups());
        }

        return updatedGroup;
    }

    public List<Groups> findGroupsByIds(Set<String> groupIds) {
        System.out.println("Group IDs encontrados para o usuário: " + groupIds);
        List<Groups> foundGroups = groupsRepository.findAllById(groupIds);
        System.out.println("Grupos encontrados: " + foundGroups);
        return foundGroups;
    }
}
