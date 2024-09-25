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

    // Método para criar ou atualizar um grupo com todos os dados
    public Groups createGroup(String nameGroup, TypeGroup typeGroup, Date startDate, Date endDate, Set<String> groupMembers) {
        // Verificar o tipo de grupo e validar as datas
        if (typeGroup == TypeGroup.VIAGEM) {
            if (startDate == null || endDate == null) {
                throw new GroupsException("Grupos do tipo 'VIAGEM' devem conter Datas de começo e fim.");
            }
        }

        // Criar novo grupo
        Groups newGroup = new Groups();
        newGroup.setNameGroup(nameGroup);
        newGroup.setTypeGroup(typeGroup);
        newGroup.setStartDate(startDate);
        newGroup.setEndDate(endDate);
        newGroup.setGroupMembers(groupMembers);
        newGroup.setExpenses(new ArrayList<>());

        // Salvar o grupo no banco de dados
        Groups savedGroup = groupsRepository.save(newGroup);

        // Atualizar cada usuário para adicionar o grupo no campo 'groups'
        for (String userId : groupMembers) {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new GroupsException("Usuário com ID " + userId + " não encontrado."));

            Set<String> userGroups = user.getGroups();
            if (userGroups == null) {
                userGroups = new HashSet<>();
            }

            userGroups.add(savedGroup.getId());
            user.setGroups(userGroups);

            // Salvar o usuário atualizado
            usersRepository.save(user);
        }

        return savedGroup;
    }



    // Deletar um grupo
    public void deleteGroup(String id) {
        // Verificar se o grupo existe
        Groups group = groupsRepository.findById(id)
                .orElseThrow(() -> new GroupsException("Grupo com ID " + id + " não encontrado."));

        // Remover o grupo da lista de grupos de cada membro do grupo
        Set<String> groupMembers = group.getGroupMembers();
        for (String userId : groupMembers) {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new GroupsException("Usuário com ID " + userId + " não encontrado."));

            Set<String> userGroups = user.getGroups();
            if (userGroups != null) {
                userGroups.remove(group.getId());
                user.setGroups(userGroups);

                // Salvar o usuário atualizado
                usersRepository.save(user);
            }
        }

        // Deletar o grupo do repositório
        groupsRepository.delete(group);
    }


    // Método para atualizar um grupo
    public Groups updateGroup(String id, UpdateGroupRequestDTO updateGroupRequestDTO) {
        Groups group = groupsRepository.findById(id)
                .orElseThrow(() -> new GroupsException("Grupo com ID " + id + " não encontrado."));

        // Atualiza os campos conforme o DTO
        if (updateGroupRequestDTO.getNameGroup() != null) {
            group.setNameGroup(updateGroupRequestDTO.getNameGroup());
        }

        if (updateGroupRequestDTO.getTypeGroup() != null) {
            // Verificar o tipo de grupo e validar as datas
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


    // Adicionar mais pessoas ao grupo
    public Groups addMembers(String groupId, Set<String> newMembers) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new GroupsException("Grupo com ID " + groupId + " não encontrado."));

        // Converte a lista de novos membros para um Set
        Set<String> membersSet = new HashSet<>(newMembers);

        // Adiciona os novos membros ao grupo
        group.getGroupMembers().addAll(membersSet);

        // Atualiza o grupo no banco de dados
        Groups updatedGroup = groupsRepository.save(group);

        // Atualiza os usuários com o novo grupo
        for (String userId : membersSet) {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new GroupsException("Usuário com ID " + userId + " não encontrado."));

            Set<String> userGroups = user.getGroups();
            if (userGroups == null) {
                userGroups = new HashSet<>();
            }

            userGroups.add(updatedGroup.getId());
            user.setGroups(userGroups);

            // Salva o usuário atualizado
            usersRepository.save(user);
        }

        return updatedGroup;
    }


    public List<Groups> findGroupsByIds(Set<String> groupIds) {
        return groupsRepository.findAllById(groupIds);
    }


}
