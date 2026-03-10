package me.isoham.taskmanager.repository;

import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByUserOrderByStatusAscIdAsc(User user);

    Optional<Task> findByIdAndUser(int id, User user);

    void deleteByIdAndUser(int id, User user);
}