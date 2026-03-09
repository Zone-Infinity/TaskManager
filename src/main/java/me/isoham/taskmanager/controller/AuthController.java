package me.isoham.taskmanager.controller;

import me.isoham.taskmanager.dto.LoginRequest;
import me.isoham.taskmanager.dto.LoginResponse;
import me.isoham.taskmanager.dto.UserResponse;
import me.isoham.taskmanager.model.User;
import me.isoham.taskmanager.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = userService.login(
                request.username(),
                request.password()
        );

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getUsername()
        );

        // TODO: Make JWT Token
        return new LoginResponse(null, userResponse);
    }
}