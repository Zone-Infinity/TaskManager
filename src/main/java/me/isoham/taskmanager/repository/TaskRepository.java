package me.isoham.taskmanager.repository;

import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByUserOrderByStatusAscIdAsc(User user);
}