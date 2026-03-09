package me.isoham.taskmanager.controller;

import me.isoham.taskmanager.dto.TaskRequest;
import me.isoham.taskmanager.dto.TaskResponse;
import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.TaskStatus;
import me.isoham.taskmanager.model.User;
import me.isoham.taskmanager.repository.UserRepository;
import me.isoham.taskmanager.service.TaskService;
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

    @PostMapping
    public TaskResponse createTask(@RequestBody TaskRequest request) {

        User user = userRepository
                .findById(request.userId())
                .orElseThrow();

        Task task = taskService.createTask(
                request.title(),
                request.description(),
                TaskStatus.valueOf(request.status()),
                user
        );

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name()
        );
    }

    @GetMapping("/{userId}")
    public List<TaskResponse> getTasks(@PathVariable int userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow();

        return taskService.getTasks(user)
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus().name()
                ))
                .toList();
    }

    @PatchMapping("/{taskId}")
    public TaskResponse updateStatus(
            @PathVariable int taskId,
            @RequestParam String status
    ) {
        Task task = taskService.updateStatus(
                taskId,
                TaskStatus.valueOf(status)
        );

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name()
        );
    }
}
