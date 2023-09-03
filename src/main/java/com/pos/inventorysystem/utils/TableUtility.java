package com.pos.inventorysystem.utils;

import com.pos.inventorysystem.db.db;

import java.sql.*;

public class TableUtility {
    public static boolean checkTableExists(String tableName) throws SQLException, ClassNotFoundException{
        Connection connection = null;
        try{
            connection = db.myconnect();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, tableName, null);
            return resultSet.next();
        } catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        } finally {
            if(connection != null){
                try{
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static void createTable(String createTableQuery) throws SQLException, ClassNotFoundException{
        try{
            Connection connection = db.myconnect();
            Statement statement = connection.createStatement();
            statement.execute(createTableQuery);
            connection.close();

        } catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
