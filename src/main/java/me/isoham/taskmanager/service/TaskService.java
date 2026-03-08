package me.isoham.taskmanager.service;

import me.isoham.taskmanager.dao.TaskDAO;
import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public boolean createTask(String title, String description, TaskStatus status, int userId) {
        Task task = new Task(title, description, status, userId);

        boolean created = taskDAO.createTask(task);

        if (created) {
            LOGGER.info("Task '{}' created for user {}", title, userId);
        } else {
            LOGGER.warn("Failed to create task '{}' for user {}", title, userId);
        }

        return created;
    }

    public List<Task> getTasksForUser(int userId) {
        return taskDAO.getTasksByUserId(userId);
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