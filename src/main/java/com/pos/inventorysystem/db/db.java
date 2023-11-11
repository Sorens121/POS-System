package com.pos.inventorysystem.db;

import com.pos.inventorysystem.utils.ConfigFileManager;

import java.sql.*;

public class db {
    public static Connection establishConnection() throws SQLException {
        ConfigFileManager configFileManager = new ConfigFileManager();
        Connection connection = null;

        try{
            String url = configFileManager.getProperty("database.url");
            String user = configFileManager.getProperty("database.username");
            String password = configFileManager.getProperty("database.password");

            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
        } catch(SQLException e) {
            System.out.println("Connection error " + e.getMessage());
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection(Connection connection, Statement statement, PreparedStatement preparedStatement, ResultSet resultSet) {
        if(resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
