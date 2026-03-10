package me.isoham.taskmanager.controller;

import me.isoham.taskmanager.dto.TaskRequest;
import me.isoham.taskmanager.dto.TaskResponse;
import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.TaskStatus;
import me.isoham.taskmanager.model.User;
import me.isoham.taskmanager.repository.UserRepository;
import me.isoham.taskmanager.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication auth) {
        Integer userId = (Integer) auth.getPrincipal();
        return userRepository.findById(userId).orElseThrow();
    }

    private TaskResponse mapTask(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name()
        );
    }

    @PostMapping
    public TaskResponse createTask(
            @RequestBody TaskRequest request,
            Authentication auth
    ) {
        User user = getCurrentUser(auth);

        Task task = taskService.createTask(
                request.title(),
                request.description(),
                TaskStatus.valueOf(request.status()),
                user
        );

        return mapTask(task);
    }

    @GetMapping
    public List<TaskResponse> getTasks(Authentication auth) {
        User user = getCurrentUser(auth);

        return taskService.getTasks(user)
                .stream()
                .map(this::mapTask)
                .toList();
    }

    @PatchMapping("/{taskId}")
    public TaskResponse updateStatus(
            @PathVariable int taskId,
            @RequestParam String status,
            Authentication auth
    ) {
        User user = getCurrentUser(auth);

        Task task = taskService.updateStatus(
                taskId,
                TaskStatus.valueOf(status),
                user
        );

        return mapTask(task);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(
            @PathVariable int taskId,
            Authentication auth
    ) {
        User user = getCurrentUser(auth);
        taskService.deleteTask(taskId, user);
    }
}