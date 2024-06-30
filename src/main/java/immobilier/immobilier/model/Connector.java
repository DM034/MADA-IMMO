package immobilier.immobilier.model;

import java.sql.*;

public class Connector {
    static String database = "immobilier";
    static String user = "postgres";
    static String password = "366325";

    public static Connection connect() throws Exception {
        Connection connection = null;
        try {
            String driver = "org.postgresql.Driver";
            String inGetConnection = "jdbc:postgresql://localhost:5432/" + database;
            Class.forName(driver);
            connection = DriverManager.getConnection(inGetConnection, user, password);
            // System.out.println(connection);
            connection.setAutoCommit(false);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void Rollback(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void CloseStatement(PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void CloseConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
