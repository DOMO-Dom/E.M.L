package resources.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class for Establishing SystemDatabase Connection
 */
public class DatabaseConnection {

    /**
     * Database Default Constructor
     */
    private DatabaseConnection(){

    }

    /**
     * Instance getter method
     * @return DatabaseConnection
     */
    public static DatabaseConnection getInstance(){
        return new DatabaseConnection();
    }

    /**
     * Establishing connection with the system database
     * @return sql connection
     */
    public Connection getConnection() {
        String connect_string = "jdbc:sqlite:src/resources/database/system_database.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connect_string);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
