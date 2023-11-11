package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Customer;
import com.pos.inventorysystem.actions.CustomerActions;
import javafx.collections.ObservableList;

import java.sql.SQLException;


public class CustomerHelper {
   public static ObservableList<Customer> getAllRecords() throws SQLException{
       CustomerActions actions = new CustomerActions();
       try{
           return actions.getAllRecords();
       } catch(SQLException e) {
           System.out.println("Error showing table");
           e.printStackTrace();
           throw e;
       }
   }

    public static ObservableList<Customer> getSearchedList(String input) throws SQLException {
        CustomerActions actions = new CustomerActions();
        try{
            return actions.searchRecord(input);
        } catch(SQLException e){
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }
}
