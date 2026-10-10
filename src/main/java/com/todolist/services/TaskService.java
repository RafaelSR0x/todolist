package com.todolist.services;

import com.todolist.entities.Tasks;
import com.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Tasks create(Tasks request) {
        return taskRepository.save(request);
    }
}
