package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Product;
import com.pos.inventorysystem.actions.ProductActions;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ProductHelper {
    public static ObservableList<Product> getAllRecords() throws SQLException {
        ProductActions actions = new ProductActions();
        try{
            return actions.getAllRecords();
        } catch (SQLException e) {
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }

    public static ObservableList<Product> getSearchedList(String input) throws SQLException {
        ProductActions actions = new ProductActions();
        try{
            return actions.searchRecord(input);
        } catch (SQLException e){
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }
}
