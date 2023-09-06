package com.pos.inventorysystem.db;

import com.pos.inventorysystem.utils.ConfigFileManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class db {
    public static Connection myConnection() throws SQLException, ClassNotFoundException {
        ConfigFileManager configFileManager = new ConfigFileManager();
        try{
            String url = configFileManager.getProperty("database.url");
            String user = configFileManager.getProperty("database.username");
            String password = configFileManager.getProperty("database.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch(SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
