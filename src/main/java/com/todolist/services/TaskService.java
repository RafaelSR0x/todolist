package com.todolist.services;

import com.todolist.dtos.TasksDTO;
import com.todolist.entities.Tasks;
import com.todolist.repositories.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Tasks create(Tasks request) {
        return taskRepository.save(request);
    }

    @Transactional(readOnly = true)
    public List<TasksDTO> findAllTask(){
        return taskRepository.findAll().stream().map(TasksDTO::new).toList();
    }

    public Tasks findById(Long taskId) {
        return taskRepository.findById(taskId).
                orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada"));
    }
}
