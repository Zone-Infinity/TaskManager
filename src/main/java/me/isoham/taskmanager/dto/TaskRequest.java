package me.isoham.taskmanager.dto;

public record TaskRequest(
        String title,
        String description,
        String status,
        int userId
) {
}
