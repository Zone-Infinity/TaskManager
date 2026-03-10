package me.isoham.taskmanager.controller;

import me.isoham.taskmanager.dto.TaskRequest;
import me.isoham.taskmanager.dto.TaskResponse;
import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.TaskStatus;
import me.isoham.taskmanager.model.User;
import me.isoham.taskmanager.security.AuthenticatedUser;
import me.isoham.taskmanager.service.TaskService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
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
            @AuthenticationPrincipal AuthenticatedUser authUser
    ) {
        User user = authUser.user();

        Task task = taskService.createTask(
                request.title(),
                request.description(),
                TaskStatus.valueOf(request.status()),
                user
        );

        return mapTask(task);
    }

    @GetMapping
    public List<TaskResponse> getTasks(
            @AuthenticationPrincipal AuthenticatedUser authUser
    ) {
        User user = authUser.user();

        return taskService.getTasks(user)
                .stream()
                .map(this::mapTask)
                .toList();
    }

    @PatchMapping("/{taskId}")
    public TaskResponse updateStatus(
            @PathVariable int taskId,
            @RequestParam String status,
            @AuthenticationPrincipal AuthenticatedUser authUser
    ) {
        User user = authUser.user();

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
            @AuthenticationPrincipal AuthenticatedUser authUser
    ) {
        User user = authUser.user();
        taskService.deleteTask(taskId, user);
    }
}
