package me.isoham.taskmanager.controller;

import me.isoham.taskmanager.dto.RegisterRequest;
import me.isoham.taskmanager.dto.UserResponse;
import me.isoham.taskmanager.model.User;
import me.isoham.taskmanager.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        User user = userService.register(
                request.username(),
                request.password()
        );

        return new UserResponse(
                user.getId(),
                user.getUsername()
        );
    }
}