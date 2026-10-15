package com.todolist.controllers;

import com.todolist.dtos.TasksDTO;
import com.todolist.entities.Tasks;
import com.todolist.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping()
    public ResponseEntity<Tasks> createTask(@Valid @RequestBody Tasks request) {
        Tasks tasks = taskService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(tasks);
    }

    @GetMapping()
    public ResponseEntity findAll() {
        List<TasksDTO> list = taskService.findAllTask();

        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity findById(@PathVariable Long taskId) {
        try {
            TasksDTO task = taskService.findById(taskId);
            return ResponseEntity.ok(task);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity updateTask(@Valid @RequestBody Tasks request, @PathVariable Long taskId){
        try {
            Tasks tasks = taskService.updateTask(request, taskId);
            return ResponseEntity.ok(tasks);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity deleteTask(@PathVariable Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PatchMapping("/{taskId}/completed")
    public ResponseEntity<TasksDTO> markAsCompleted(@PathVariable Long taskId) {
        Tasks tasks = taskService.markAsRead(taskId);
        TasksDTO dto = new TasksDTO(tasks);

        return ResponseEntity.ok(dto);
    }
}
