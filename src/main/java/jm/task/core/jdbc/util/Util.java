package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    static String url = "jdbc:mysql://localhost:3306";
    static String user = "root";
    static String password = "root";

    private Util() {
    }

    public static Connection getConnection()  {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

