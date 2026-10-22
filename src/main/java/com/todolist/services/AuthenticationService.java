package com.todolist.services;

import com.todolist.dtos.RegisterDTO;
import com.todolist.entities.User;
import com.todolist.entities.enums.UserRole;
import com.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository repository;

    public User userRegister(RegisterDTO data) {
        if (this.repository.findByEmail(data.email()) != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este email já existe.");
        }

        String encrypetedPassword = new BCryptPasswordEncoder().encode(data.password());

        User newUser = new User(data.email(), encrypetedPassword, UserRole.USER);

        return repository.save(newUser);
    }


}
