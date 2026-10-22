package com.todolist.services;

import com.todolist.dtos.TasksDTO;
import com.todolist.dtos.TasksResponseDTO;
import com.todolist.entities.Tasks;
import com.todolist.entities.User;
import com.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    public TasksResponseDTO create(Tasks request) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        request.setUser(user);

        Tasks tasksSaved = taskRepository.save(request);

        return new TasksResponseDTO(
                tasksSaved.getId(),
                tasksSaved.getTitle(),
                tasksSaved.getDescription(),
                tasksSaved.isCompleted(),
                tasksSaved.getCreatedAt(),
                tasksSaved.getUpdatedAt()
        );
    }

    //Metodo para listar todos as tarefas com paginação
    @Transactional(readOnly = true)
    public Page<TasksDTO> findAllPaged(Boolean completed, String title,Pageable pageable){
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Page<Tasks> tasksPage = taskRepository.findAllFiltered(
                completed,
                title,
                user.getId(),
                pageable);

        return tasksPage.map(TasksDTO::new);
    }

    //Metodo para retornar apenas uma tarefa pelo ID requisitado
    public TasksDTO findById(Long taskId) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Tasks tasks = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        return new TasksDTO(tasks);
    }

    //Metodo para atualizar uma tarefa pelo ID requisitado
    public Tasks updateTask(Tasks request, Long taskId) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Tasks tasks = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        tasks.setTitle(request.getTitle());
        tasks.setDescription(request.getDescription());

        return taskRepository.save(tasks);
    }

    //Metodo para deletar uma tarefa pelo Id requisitado
    public void deleteTask(Long taskId){
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Tasks tasks = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

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
