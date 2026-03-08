package me.isoham.taskmanager.dao;

import me.isoham.taskmanager.config.DatabaseConnection;
import me.isoham.taskmanager.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);

    @Override
    public boolean createUser(User user) {
        String sql = """
                INSERT INTO users (username, password)
                VALUES (?, ?)
                """;

        try (var conn = DatabaseConnection.getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected == 1;
        } catch (SQLException e) {
            LOGGER.error("Error creating user", e);
            return false;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = """
                SELECT id, username, password
                FROM users
                WHERE username = ?
                """;

        try (var conn = DatabaseConnection.getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                User user = new User(rs.getString("username"), rs.getString("password"));
                user.setId(rs.getInt("id"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding user by username", e);
        }

        return Optional.empty();
    }
}
