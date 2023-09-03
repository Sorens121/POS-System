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

    public static ObservableList<Customer> getSearchedList(String id) throws ClassNotFoundException, SQLException{
        CustomerActions actions = new CustomerActions();
        try{
            ResultSet resultSet = actions.searchCustomerById(id);
            ObservableList<Customer> list = getAllCustomerList(resultSet);
            return list;
        } catch(SQLException e){
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }

    public static ObservableList<Customer> getSearchedListByDetails(String name, String tp) throws ClassNotFoundException, SQLException{
        CustomerActions actions = new CustomerActions();
        try{
            ResultSet resultSet = actions.searchCustomerByDetails(name, tp);
            ObservableList<Customer> list = getAllCustomerList(resultSet);
            return list;
        } catch(SQLException |ClassNotFoundException e){
            System.out.println("Error while showing table");
            e.printStackTrace();
            throw e;
        }
    }

    private static ObservableList<Customer> getAllCustomerList(ResultSet resultSet) throws ClassNotFoundException,SQLException {
       try{
           ObservableList<Customer> customerList = FXCollections.observableArrayList();
           while(resultSet.next()){
               Customer customer = new Customer();
               customer.setCustomerId(resultSet.getString("cid"));
               customer.setCustomerName(resultSet.getString("customer_name"));
               customer.setTpNumber(resultSet.getString("Tp_Number"));
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
