package me.isoham.taskmanager.dao;

import me.isoham.taskmanager.model.User;

import java.util.Optional;

public interface UserDAO {

    boolean createUser(User user);

    Optional<User> findByUsername(String username);
}
