package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Customer;
import com.pos.inventorysystem.actions.CustomerActions;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class CustomerPopUpHelper {
    public static ObservableList<Customer> searchCustomerRecords(String searchInput) throws SQLException {
        CustomerActions actions = new CustomerActions();
        try{
            return actions.searchRecord(searchInput);
        } catch(SQLException e) {
            System.out.println("Error loading data for pop-up table");
            e.printStackTrace();
            throw e;
        }
    }
}
