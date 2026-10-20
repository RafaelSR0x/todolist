package com.todolist.dtos;

import com.todolist.entities.enums.UserRole;

public record RegisterDTO(String email, String password, UserRole role) {
}
