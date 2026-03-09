package me.isoham.taskmanager.service;

import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.TaskStatus;
import me.isoham.taskmanager.model.User;
import me.isoham.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks(User user) {
        return taskRepository.findByUserOrderByStatusAscIdAsc(user);
    }

    public Task createTask(String title, String description, TaskStatus status, User user) {
        Task task = new Task(title, description, status, user);
        return taskRepository.save(task);
    }

    public Task updateStatus(int taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setStatus(status);

        return taskRepository.save(task);
    }

    public void deleteTask(int taskId) {
        taskRepository.deleteById(taskId);
    }
}