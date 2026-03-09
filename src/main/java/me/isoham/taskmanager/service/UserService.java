package me.isoham.taskmanager.service;

import me.isoham.taskmanager.model.User;
import me.isoham.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String password) {
        User user = new User(username, password);

        return userRepository.save(user);
    }
}