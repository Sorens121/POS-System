package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Customer;
import com.pos.inventorysystem.actions.CustomerActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerHelper {
    public static ObservableList<Customer> getAllRecords() throws ClassNotFoundException, SQLException{
       CustomerActions actions = new CustomerActions();
       try{
           ResultSet resultSet = actions.getAllCustomers();
           ObservableList<Customer> list = getAllCustomerList(resultSet);
           return list;
       } catch(SQLException e) {
           System.out.println("Error showing table");
           e.printStackTrace();
           throw e;
       }
   }

    public static ObservableList<Customer> getSearchedList(String input) throws ClassNotFoundException, SQLException{
        CustomerActions actions = new CustomerActions();
        try{
            ResultSet resultSet = actions.searchCustomer(input);
            ObservableList<Customer> list = getAllCustomerList(resultSet);
            return list;
        } catch(SQLException e){
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }


    private static ObservableList<Customer> getAllCustomerList(ResultSet resultSet) throws ClassNotFoundException,SQLException {
       try{
           ObservableList<Customer> customerList = FXCollections.observableArrayList();
           while(resultSet.next()){
               Customer customer = new Customer();
               customer.setCustomerId(resultSet.getString("customer_id"));
               customer.setCustomerName(resultSet.getString("customer_name"));
               customer.setContactNumber(resultSet.getString("contact_no"));
               customer.setEmail(resultSet.getString("email"));
               customerList.add(customer);
           }

           return customerList;
       } catch(SQLException e){
           System.out.println("Error showing table");
           e.printStackTrace();
           throw e;
       }
    }
}
