package com.example.traveltrab_backend_mongo.entities.groups.service;

import com.example.traveltrab_backend_mongo.DTOS.GroupsRequestDTO;
import com.example.traveltrab_backend_mongo.DTOS.UpdateGroupRequestDTO;
import com.example.traveltrab_backend_mongo.entities.expenses.domain.Expenses;
import com.example.traveltrab_backend_mongo.entities.expenses.service.ExpensesService;
import com.example.traveltrab_backend_mongo.entities.groups.Tasks;
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

    @Autowired
    private ExpensesService expensesService;

    public Groups createGroup(String nameGroup, TypeGroup typeGroup, Date startDate, Date endDate, List<Tasks> tasks, Set<String> groupMembers) {
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
        newGroup.setTasks(new ArrayList<>());
        newGroup.setGroupMembers(groupMembers);
        newGroup.setExpenses(new ArrayList<>());

        Groups savedGroup = groupsRepository.save(newGroup);

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
        }

        return savedGroup;
    }

    public void deleteGroup(String id) {
        Groups group = groupsRepository.findById(id)
                .orElseThrow(() -> new GroupsException("Grupo com ID " + id + " não encontrado."));

        // Fetch all expenses associated with this group
        List<Expenses> expensesToDelete = group.getExpenses();

        // Delete each expense associated with the group
        if (expensesToDelete != null && !expensesToDelete.isEmpty()) {
            for (Expenses expense : expensesToDelete) {
                expensesService.deleteExpense(expense.getId());
            }
        }

        // Remove the group from users' group lists
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

        // Finally, delete the group
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
        }

        return updatedGroup;
    }

    public Groups removeMember(String groupId, String userId) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new GroupsException("Grupo com ID " + groupId + " não encontrado."));

        if (!group.getGroupMembers().contains(userId)) {
            throw new GroupsException("Usuário com ID " + userId + " não está no grupo.");
        }

        group.getGroupMembers().remove(userId);
        Groups updatedGroup = groupsRepository.save(group);

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new GroupsException("Usuário com ID " + userId + " não encontrado."));

        Set<String> userGroups = user.getGroups();
        if (userGroups != null) {
            userGroups.remove(group.getId());
            user.setGroups(userGroups);
            usersRepository.save(user);
        }

        return updatedGroup;
    }

    public List<Groups> findGroupsByIds(Set<String> groupIds) {
        System.out.println("Group IDs encontrados para o usuário: " + groupIds);
        List<Groups> foundGroups = groupsRepository.findAllById(groupIds);
        System.out.println("Grupos encontrados: " + foundGroups);
        return foundGroups;
    }


    public Groups createTask(String groupId, Tasks newTask) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new GroupsException("Grupo com ID " + groupId + " não encontrado."));

        // Adiciona a nova tarefa à lista de tarefas do grupo
        if (group.getTasks() == null) {
            group.setTasks(new ArrayList<>());
        }
        group.getTasks().add(newTask);

        // Salva o grupo com a nova tarefa
        return groupsRepository.save(group);
    }

    // Atualiza uma tarefa existente dentro do grupo
    // Atualiza uma tarefa existente
    public Groups updateTask(String groupId, String taskId, Tasks updatedTask) {
        // Localiza o grupo pelo ID
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new GroupsException("Grupo com ID " + groupId + " não encontrado."));

        // Localiza a tarefa dentro do grupo pelo ID
        Tasks task = group.getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new GroupsException("Tarefa com ID " + taskId + " não encontrada no grupo."));

        // Atualiza os dados da tarefa, mas apenas se o campo for diferente de null
        if (updatedTask.getName() != null) {
            task.setName(updatedTask.getName());
        }
        if (updatedTask.getStartDate() != null) {
            task.setStartDate(updatedTask.getStartDate());
        }
        if (updatedTask.getEndDate() != null) {
            task.setEndDate(updatedTask.getEndDate());
        }
        if (updatedTask.getIsDone() != null) {
            task.setIsDone(updatedTask.getIsDone());
        }

        // Salva o grupo com a tarefa atualizada
        return groupsRepository.save(group);  // Aqui, o grupo é salvo com as tarefas atualizadas
    }



    public Groups deleteTask(String groupId, String taskId) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new GroupsException("Grupo com ID " + groupId + " não encontrado."));

        // Remove a tarefa com o ID especificado da lista de tarefas
        boolean taskRemoved = group.getTasks().removeIf(task -> task.getId().equals(taskId));

        if (!taskRemoved) {
            throw new GroupsException("Tarefa com ID " + taskId + " não encontrada no grupo.");
        }

        // Salva o grupo com a tarefa removida
        return groupsRepository.save(group);
    }
    public Optional<Groups> findGroupById(String groupId) {
        return groupsRepository.findById(groupId);
    }
}


