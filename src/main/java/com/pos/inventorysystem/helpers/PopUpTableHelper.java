package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Product;
import com.pos.inventorysystem.actions.PopUpTableActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PopUpTableHelper {
    public static ObservableList<Product> getAllSearchList(String searchInput) throws SQLException {
        PopUpTableActions actions = new PopUpTableActions();
        try {
            return actions.getAllRecords(searchInput);
        } catch (SQLException e) {
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }
}
