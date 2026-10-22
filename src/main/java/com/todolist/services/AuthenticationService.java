package com.todolist.services;

import com.todolist.dtos.RegisterDTO;
import com.todolist.entities.User;
import com.todolist.entities.enums.UserRole;
import com.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User userRegister(RegisterDTO data) {
        if (this.repository.findByEmail(data.email()) != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este email já existe.");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());

        User newUser = new User(data.email(), encryptedPassword, UserRole.USER);

        return repository.save(newUser);
    }


}
