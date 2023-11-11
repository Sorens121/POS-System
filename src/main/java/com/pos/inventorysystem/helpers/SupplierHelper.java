package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Supplier;
import com.pos.inventorysystem.actions.SupplierActions;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class SupplierHelper {
    public static ObservableList<Supplier> getAllSuppliersList() throws SQLException {
        SupplierActions actions = new SupplierActions();

        try {
            return actions.getAllRecords();
        } catch (SQLException e) {
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }

    public static ObservableList<Supplier> getAllSearchedList(String input) throws SQLException {
        SupplierActions actions = new SupplierActions();

        try{
            return actions.searchRecord(input);
        } catch (SQLException e) {
            System.out.println("Error showing table");
            throw e;
        }
    }
}
