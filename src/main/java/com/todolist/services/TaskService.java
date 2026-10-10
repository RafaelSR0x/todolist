package com.todolist.services;

import com.todolist.dtos.TasksDTO;
import com.todolist.entities.Tasks;
import com.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Tasks create(Tasks request) {
        return taskRepository.save(request);
    }

    public List<TasksDTO> listTasks(){}
}
