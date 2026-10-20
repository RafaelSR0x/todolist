package com.todolist.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    private String id;
    private String email;
    private String password;
    private String role;
}
