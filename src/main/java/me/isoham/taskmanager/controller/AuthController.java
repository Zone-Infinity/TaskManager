package me.isoham.taskmanager.controller;

import me.isoham.taskmanager.dto.LoginRequest;
import me.isoham.taskmanager.dto.LoginResponse;
import me.isoham.taskmanager.dto.UserResponse;
import me.isoham.taskmanager.model.User;
import me.isoham.taskmanager.security.JwtService;
import me.isoham.taskmanager.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = userService.login(
                request.username(),
                request.password()
        );

        String token = jwtService.generateToken(user);

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getUsername()
        );

        return new LoginResponse(token, userResponse);
    }
}