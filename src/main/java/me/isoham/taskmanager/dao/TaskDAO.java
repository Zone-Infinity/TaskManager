package me.isoham.taskmanager.dao;

import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.TaskStatus;

import java.util.List;

public interface TaskDAO {

    boolean createTask(Task task);

    List<Task> getTasksByUserId(int userId);

    boolean updateTaskStatus(int taskId, TaskStatus status);

    boolean deleteTask(int taskId);
}
