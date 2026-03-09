package me.isoham.taskmanager.service;

import me.isoham.taskmanager.model.User;
import me.isoham.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String password) {
        String hash = encoder.encode(password);
        User user = new User(username, hash);

        return userRepository.save(user);
    }

    public User login(String username, String password) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }
}