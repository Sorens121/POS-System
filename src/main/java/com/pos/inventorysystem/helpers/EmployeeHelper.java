package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Employee;
import com.pos.inventorysystem.actions.EmployeeActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeHelper {
    public static ObservableList<Employee> getAllRecords() throws SQLException {
        EmployeeActions actions = new EmployeeActions();
        try {
            return actions.getAllRecords();
        } catch (SQLException e) {
            System.out.println("Error showing table: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static ObservableList<Employee> getSearchedList(String input) throws SQLException{
        EmployeeActions actions = new EmployeeActions();
        try{
            return actions.searchRecord(input);
        } catch(SQLException e){
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }
}
