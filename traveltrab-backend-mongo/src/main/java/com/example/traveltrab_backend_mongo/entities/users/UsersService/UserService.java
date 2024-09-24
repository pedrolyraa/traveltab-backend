package com.example.traveltrab_backend_mongo.entities.users.UsersService;

import com.example.traveltrab_backend_mongo.entities.users.domain.Users;
import com.example.traveltrab_backend_mongo.entities.users.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsersRepository userRepository;


    public String sendFriendRequest(String targetEmail){

        String requesterEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Users> requesterOpt = userRepository.findByEmail(requesterEmail);
        Optional<Users> targetOpt = userRepository.findByEmail(targetEmail);

        if (requesterOpt.isEmpty() || targetOpt.isEmpty()) {
            return "Usuário não encontrado";
        }

        Users requester = requesterOpt.get();
        Users target = targetOpt.get();

        if (target.getFriendList().contains(requester.getId())) {
            return "Vocês já são amigos";
        }

        if (target.getFriendRequests().contains(requester.getId())) {
            return "Solicitação de amizade já enviada";
        }

        target.getFriendRequests().add(requester.getId());
        userRepository.save(target);

        return "Solicitação de amizade enviada";
    }
}
