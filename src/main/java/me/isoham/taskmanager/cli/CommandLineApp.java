package me.isoham.taskmanager.cli;

import me.isoham.taskmanager.dao.TaskDAOImpl;
import me.isoham.taskmanager.dao.UserDAOImpl;
import me.isoham.taskmanager.model.TaskStatus;
import me.isoham.taskmanager.model.User;
import me.isoham.taskmanager.service.TaskService;
import me.isoham.taskmanager.service.UserService;

import java.util.Optional;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class CommandLineApp {
    private final UserService userService;
    private final TaskService taskService;
    private final Scanner scanner;
    private User currentUser;

    public CommandLineApp() {
        userService = new UserService(new UserDAOImpl());
        taskService = new TaskService(new TaskDAOImpl());
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            if (currentUser == null) {
                showAuthMenu();
            } else {
                showUserMenu();
            }
            System.out.println("------------------------------------------------------------");
        }
    }

    private void showAuthMenu() {
        System.out.println("""
                1 Register
                2 Login
                3 Exit
                """);

        int choice;

        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input a number!");
            return;
        }

        switch (choice) {
            case 1 -> handleRegister();
            case 2 -> handleLogin();
            case 3 -> System.exit(0);
            default -> System.out.println("Invalid choice");
        }
    }

    private void showUserMenu() {
        System.out.printf("""
                Logged in as %s
                
                1 Create task
                2 List tasks
                3 Update task status
                4 Delete task
                5 Logout
                6 Exit
                """, currentUser.getUsername());

        int choice;

        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input a number!");
            return;
        }

        switch (choice) {
            case 1 -> handleCreateTask();
            case 2 -> handleListTasks();
            case 3 -> handleUpdateTask();
            case 4 -> handleDeleteTask();
            case 5 -> logout();
            case 6 -> System.exit(0);
            default -> System.out.println("Invalid choice");
        }
    }

    private void handleRegister() {
        inputUserThen((username, password) -> {
            if (userService.registerUser(username, password)) System.out.printf("%s created.%n", username);
            else System.out.println("Failed to create username.\n" + "Try another username.");
        });
    }

    private void handleLogin() {
        if (currentUser != null) {
            System.out.printf("Already logged in as %s%n", currentUser.getUsername());
            return;
        }

        inputUserThen((username, password) -> {
            Optional<User> optionalUser = userService.loginUser(username, password);

            optionalUser.ifPresentOrElse((user) -> {
                currentUser = user;
                System.out.printf("Welcome %s%n", user.getUsername());
            }, () -> System.out.println("Login failed. Invalid username or password."));
        });
    }

    private void inputUserThen(BiConsumer<String, String> callback) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        callback.accept(username, password);
    }

    private void logout() {
        if (currentUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        System.out.printf("Goodbye %s%n", currentUser.getUsername());
        currentUser = null;
    }

    private void handleCreateTask() {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        boolean created = taskService.createTask(
                title,
                description,
                TaskStatus.TODO,
                currentUser.getId()
        );

        if (created) {
            System.out.println("Task created.");
        } else {
            System.out.println("Failed to create task.");
        }
    }

    private void handleListTasks() {
        var tasks = taskService.getTasksForUser(currentUser.getId());

        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
            return;
        }

        for (var task : tasks) {
            System.out.printf(
                    "[%d] %s (%s)%n",
                    task.getId(),
                    task.getTitle(),
                    task.getStatus()
            );
        }
    }

    private void handleUpdateTask() {
        System.out.print("Task ID: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        System.out.println("Status:");
        System.out.println("1 TODO");
        System.out.println("2 IN_PROGRESS");
        System.out.println("3 DONE");

        int choice = Integer.parseInt(scanner.nextLine());

        TaskStatus status = switch (choice) {
            case 1 -> TaskStatus.TODO;
            case 2 -> TaskStatus.IN_PROGRESS;
            case 3 -> TaskStatus.DONE;
            default -> {
                System.out.println("Invalid status.");
                yield null;
            }
        };

        if (status == null) return;

        boolean updated = taskService.updateTaskStatus(taskId, status);

        System.out.println(updated ? "Task updated." : "Failed to update task.");
    }

    private void handleDeleteTask() {
        System.out.print("Task ID: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        boolean deleted = taskService.deleteTask(taskId);

        System.out.println(deleted ? "Task deleted." : "Failed to delete task.");
    }
}
