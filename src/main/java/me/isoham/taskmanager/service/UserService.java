package me.isoham.taskmanager.service;

import me.isoham.taskmanager.dao.UserDAO;
import me.isoham.taskmanager.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserDAO dao;

    public UserService(UserDAO dao) {
        this.dao = dao;
    }

    public Optional<User> loginUser(String username, String password) {
        Optional<User> optionalUser = dao.findByUsername(username);
        if (optionalUser.isEmpty()) return Optional.empty();

        User user = optionalUser.get();
        if (!password.equals(user.getPassword())) return Optional.empty();

        return Optional.of(user);
    }

    public boolean registerUser(String username, String password) {
        Optional<User> optionalUser = dao.findByUsername(username);
        if (optionalUser.isPresent()) {
            LOGGER.warn("Username {} already exists", username);
            return false;
        }

        boolean created = dao.createUser(new User(username, password));
        if (created) LOGGER.info("User '{}' registered successfully", username);
        return created;
    }
}
