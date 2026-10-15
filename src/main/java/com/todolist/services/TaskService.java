package com.todolist.services;

import com.todolist.dtos.TasksDTO;
import com.todolist.entities.Tasks;
import com.todolist.repositories.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    //Metodo para criar nova tarefa
    public Tasks create(Tasks request) {
        return taskRepository.save(request);
    }

    //Metodo para listar todos as tarefas
    @Transactional(readOnly = true)
    public List<TasksDTO> findAllTask(){
        return taskRepository.findAll().stream().map(TasksDTO::new).toList();
    }

    //Metodo para retornar apenas uma tarefa pelo ID requisitado
    public TasksDTO findById(Long taskId) {
        Tasks tasks = taskRepository.findById(taskId).
                orElseThrow(() -> new EntityNotFoundException("o item com o ID informado não foi encontrado."));

        return new TasksDTO(tasks);
    }

    //Metodo para atualizar uma tarefa pelo ID requisitado
    public Tasks updateTask(Tasks request, Long taskId) {
        Tasks tasks = taskRepository.findById(taskId).
                orElseThrow(() -> new EntityNotFoundException("o item com o ID informado não foi encontrado."));

        tasks.setTitle(request.getTitle());
        tasks.setDescription(request.getDescription());

        return taskRepository.save(tasks);
    }

    //Metodo para deletar uma tarefa pelo Id requisitado
    public void deleteTask(Long taskId){
        Tasks tasks = taskRepository.findById(taskId).
                orElseThrow(() -> new EntityNotFoundException("o item com o ID informado não foi encontrado."));

        taskRepository.delete(tasks);
    }

    //metodo para marcar como concluido a tarefa
    public Tasks markAsRead(Long taskId) {
        Tasks tasks = taskRepository.findById(taskId).
                orElseThrow(() -> new EntityNotFoundException("o item com o ID informado não foi encontrado."));

        if (tasks.isCompleted()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A tarefa já está concluída"
            );
        }

        tasks.setCompleted(true);

        return taskRepository.save(tasks);

    }
}
