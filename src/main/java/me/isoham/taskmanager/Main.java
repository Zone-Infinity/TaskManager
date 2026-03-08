package me.isoham.taskmanager;

import me.isoham.taskmanager.cli.CommandLineApp;
import me.isoham.taskmanager.config.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    static void main() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();

        if (conn != null) {
            LOGGER.info("Connected to database");

            CommandLineApp app = new CommandLineApp();
            app.run();

            conn.close();
        }
    }
}
