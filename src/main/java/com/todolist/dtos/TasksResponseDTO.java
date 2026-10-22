package com.todolist.dtos;

import java.time.LocalDateTime;

public record TasksResponseDTO(
        Long id,
        String title,
        String description,
        boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
