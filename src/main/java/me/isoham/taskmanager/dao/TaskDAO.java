package me.isoham.taskmanager.dao;

import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.TaskStatus;
import me.isoham.taskmanager.model.User;

import java.util.List;

public interface TaskDAO {

    boolean createTask(Task task);

    List<Task> getTasksByUser(User user);

    boolean updateTaskStatus(int taskId, TaskStatus status);

    boolean deleteTask(int taskId);
}
