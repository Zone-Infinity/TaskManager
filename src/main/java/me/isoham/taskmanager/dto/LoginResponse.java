package me.isoham.taskmanager.dto;

public record LoginResponse(
        String token,
        UserResponse user
) {
}
