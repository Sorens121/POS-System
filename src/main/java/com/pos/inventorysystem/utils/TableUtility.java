package com.pos.inventorysystem.utils;

import com.pos.inventorysystem.db.db;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.*;
import java.util.function.Function;

public class TableUtility {

    //NO NEED THIS METHOD TABLE CHECK IS DONE FROM CONFIG FILE
    /*
    public static boolean checkTableExists(String tableName) throws SQLException, ClassNotFoundException{
        Connection connection = null;
        try{
            connection = db.myConnection();
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
    }*/

    public static void createTable(String createTableQuery) throws SQLException, ClassNotFoundException{
        try{
            Connection connection = db.myConnection();
            Statement statement = connection.createStatement();
            statement.execute(createTableQuery);
            connection.close();

        } catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static <S, T>TableColumn<S, T> createTableColumn(String columnName, Function<S, T> valueExtractor) {
        TableColumn<S, T> column = new TableColumn<>(columnName);
        column.setCellValueFactory(cellData -> {
            S rowData = cellData.getValue();
            T value = valueExtractor.apply(rowData);

            return new SimpleObjectProperty<>(value);
        });

        return column;
    }
}
