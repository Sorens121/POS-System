package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Employee;
import com.pos.inventorysystem.actions.EmployeeActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeHelper {
    public static ObservableList<Employee> getAllRecords() throws ClassNotFoundException, SQLException {
        EmployeeActions actions = new EmployeeActions();
        try {
            ResultSet resultSet = actions.getAllEmployees();
            ObservableList<Employee> list = getAllEmployeeList(resultSet);
            return list;
        } catch (SQLException e) {
            System.out.println("Error showing table: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static ObservableList<Employee> getSearchedList(String id) throws ClassNotFoundException, SQLException{
        EmployeeActions actions = new EmployeeActions();
        try{
            ResultSet resultSet = actions.searchEmployeeById(id);
            ObservableList<Employee> list = getAllEmployeeList(resultSet);
            return list;
        } catch(SQLException e){
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }

    public static ObservableList<Employee> getSearchedListByDetails(String name, String tp) throws ClassNotFoundException, SQLException{
        EmployeeActions actions = new EmployeeActions();
        try{
            ResultSet resultSet = actions.searchEmployeeByDetails(name, tp);
            ObservableList<Employee> list = getAllEmployeeList(resultSet);
            return list;
        } catch(SQLException |ClassNotFoundException e){
            System.out.println("Error while showing table");
            e.printStackTrace();
            throw e;
        }
    }

    private static ObservableList<Employee> getAllEmployeeList(ResultSet resultSet) throws SQLException {
        try{
            ObservableList<Employee> employeeList = FXCollections.observableArrayList();
            while(resultSet.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(resultSet.getString("eid"));
                employee.setEmployeeName(resultSet.getString("employee_name"));
                employee.setEmployeeTpNumber(resultSet.getString("employee_Tp_Number"));
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
