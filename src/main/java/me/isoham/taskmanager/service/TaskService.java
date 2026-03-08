package me.isoham.taskmanager.service;

import me.isoham.taskmanager.dao.TaskDAO;
import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.TaskStatus;
import me.isoham.taskmanager.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public boolean createTask(String title, String description, TaskStatus status, User user) {
        Task task = new Task(title, description, status, user);

        boolean created = taskDAO.createTask(task);

        if (created) {
            LOGGER.info("Task '{}' created for user {}", title, user.getUsername());
        } else {
            LOGGER.warn("Failed to create task '{}' for user {}", title, user.getUsername());
        }

        return created;
    }

    public List<Task> getTasksForUser(User user) {
        return taskDAO.getTasksByUser(user);
    }

    public boolean updateTaskStatus(int taskId, TaskStatus status) {
        boolean updated = taskDAO.updateTaskStatus(taskId, status);

        if (updated) {
            LOGGER.info("Task {} status updated to {}", taskId, status);
        } else {
            LOGGER.warn("Failed to update task {} status", taskId);
        }

        return updated;
    }

    public boolean deleteTask(int taskId) {
        boolean deleted = taskDAO.deleteTask(taskId);

        if (deleted) {
            LOGGER.info("Task {} deleted", taskId);
        } else {
            LOGGER.warn("Failed to delete task {}", taskId);
        }

        return deleted;
    }
}