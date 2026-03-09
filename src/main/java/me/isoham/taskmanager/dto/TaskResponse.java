package me.isoham.taskmanager.dto;

public record TaskResponse(
        int id,
        String title,
        String description,
        String status
) {
}
