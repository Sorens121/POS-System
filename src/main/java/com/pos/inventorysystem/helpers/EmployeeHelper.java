package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Employee;
import com.pos.inventorysystem.actions.EmployeeActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeHelper {
    public static ObservableList<Employee> getAllEmployeeRecords(ResultSet resultSet) throws SQLException, ClassNotFoundException{
        try {
            return getAllEmployeeList(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ResultSet getAllRecords() throws ClassNotFoundException, SQLException {
        EmployeeActions actions = new EmployeeActions();
        try {
            ResultSet resultSet = actions.getAllEmployees();
            //ObservableList<Employee> list = getAllEmployeeList(resultSet);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error showing table: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static ResultSet getSearchedList(String input) throws ClassNotFoundException, SQLException{
        EmployeeActions actions = new EmployeeActions();
        try{
            return actions.searchEmployee(input);
        } catch(SQLException e){
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }


    private static ObservableList<Employee> getAllEmployeeList(ResultSet resultSet) throws SQLException {
        try{
            ObservableList<Employee> employeeList = FXCollections.observableArrayList();
            while(resultSet.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(resultSet.getString("employee_id"));
                employee.setEmployeeName(resultSet.getString("employee_name"));
                employee.setContactNo(resultSet.getString("contact_no"));
                employee.setEmail(resultSet.getString("email"));
                employeeList.add(employee);
            }

            return employeeList;
        } catch(SQLException e) {
            System.out.println("Error showing table: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
