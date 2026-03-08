package me.isoham.taskmanager.dao;

import me.isoham.taskmanager.config.DatabaseConnection;
import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDAOImpl implements TaskDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDAOImpl.class);

    @Override
    public boolean createTask(Task task) {
        String sql = """
                INSERT INTO tasks (title, description, status, user_id)
                VALUES (?, ?, ?, ?)
                """;

        try (var conn = DatabaseConnection.getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getStatus().toString());
            stmt.setInt(4, task.getUserId());

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected == 1;
        } catch (SQLException e) {
            LOGGER.error("Error creating task", e);
            return false;
        }
    }

    @Override
    public List<Task> getTasksByUserId(int userId) {
        String sql = """
                SELECT id, title, description, status FROM tasks
                WHERE user_id = ?
                """;

        try (var conn = DatabaseConnection.getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            List<Task> tasks = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    TaskStatus status = TaskStatus.valueOf(rs.getString("status"));

                    Task task = new Task(title, description, status, userId);
                    task.setId(id);
                    tasks.add(task);
                }
            }

            return tasks;
        } catch (SQLException e) {
            LOGGER.error("Error fetching tasks", e);
            return List.of();
        }
    }

    @Override
    public boolean updateTaskStatus(int taskId, TaskStatus status) {
        String sql = """
                UPDATE tasks SET status = ?
                WHERE id = ?
                """;

        try (var conn = DatabaseConnection.getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.toString());
            stmt.setInt(2, taskId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected == 1;
        } catch (SQLException e) {
            LOGGER.error("Error updating task status", e);
            return false;
        }
    }

    @Override
    public boolean deleteTask(int taskId) {
        String sql = """
                DELETE FROM tasks
                WHERE id = ?
                """;

        try (var conn = DatabaseConnection.getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected == 1;
        } catch (SQLException e) {
            LOGGER.error("Error deleting task", e);
            return false;
        }
    }
}
