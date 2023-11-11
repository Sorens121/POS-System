package com.pos.inventorysystem.utils;

import com.pos.inventorysystem.db.db;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;

public class TableUtility {
    public static void createTable(String createTableQuery) throws SQLException {
        try{
            Connection connection = db.establishConnection();
            Statement statement = connection.createStatement();
            statement.execute(createTableQuery);
            connection.close();

        } catch(SQLException e) {
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
