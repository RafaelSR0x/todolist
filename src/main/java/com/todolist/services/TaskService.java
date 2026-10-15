package com.todolist.services;

import com.todolist.dtos.TasksDTO;
import com.todolist.entities.Tasks;
import com.todolist.repositories.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    //metodo para centralizar a busca de uma tarefa por ID
    private Tasks findTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId).
                orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "o item com o ID informado não foi encontrado."));
    }

    //Metodo para criar nova tarefa
    public Tasks create(Tasks request) {
        return taskRepository.save(request);
    }

    //Metodo para listar todos as tarefas
    @Transactional(readOnly = true)
    public List<TasksDTO> findAllTask(){
        List<TasksDTO> list = taskRepository.findAll().stream().map(TasksDTO::new).toList();

        return list;
    }

    //Metodo para retornar apenas uma tarefa pelo ID requisitado
    public TasksDTO findById(Long taskId) {
        Tasks tasks = findTaskOrThrow(taskId);

        return new TasksDTO(tasks);
    }

    //Metodo para atualizar uma tarefa pelo ID requisitado
    public Tasks updateTask(Tasks request, Long taskId) {
        Tasks tasks = findTaskOrThrow(taskId);

        tasks.setTitle(request.getTitle());
        tasks.setDescription(request.getDescription());

        return taskRepository.save(tasks);
    }

    //Metodo para deletar uma tarefa pelo Id requisitado
    public void deleteTask(Long taskId){
        Tasks tasks = findTaskOrThrow(taskId);

        taskRepository.delete(tasks);
    }

    //metodo para marcar como concluido a tarefa
    public Tasks markAsCompleted(Long taskId) {
        Tasks tasks = findTaskOrThrow(taskId);

        if (tasks.isCompleted()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A tarefa já está concluída"
            );
        }

        tasks.setCompleted(true);

        return taskRepository.save(tasks);

    }
}
