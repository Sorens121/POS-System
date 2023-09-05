package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Customer;
import com.pos.inventorysystem.actions.CustomerActions;
import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.CustomerHelper;
import com.pos.inventorysystem.utils.DialogBoxUtility;
import com.pos.inventorysystem.utils.GenericUtils;
import com.pos.inventorysystem.utils.TableUtility;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerController {

    @FXML
    private TextField search_id;

    @FXML
    private TextField customer_id_field;

    @FXML
    private TextField customer_name_field;

    @FXML
    private TextField contact_no_field;

    @FXML
    private TextField email_field;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> serial_no;

    @FXML
    private TableColumn<Customer, String> customer_name;

    @FXML
    private TableColumn<Customer, String> customer_id;

    @FXML
    private TableColumn<Customer, String> contact_no;

    @FXML
    private TableColumn<Customer, String> email;

    private ObservableList<Customer> customers = null;
    private ResultSet resultSet = null;

    DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();
    CustomerActions actions = new CustomerActions();

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE customer (customer_id VARCHAR(12) NOT NULL, customer_name VARCHAR(45), contact_no VARCHAR(10), email VARCHAR(45), PRIMARY KEY(customer_id), UNIQUE(contact_no, email))";

     @FXML
     private void initialize() throws SQLException, ClassNotFoundException{
         //check for the table
         try{
             boolean tableExist = TableUtility.checkTableExists("customer");

             if(!tableExist) {
                 TableUtility.createTable(CREATE_TABLE_QUERY);
                 System.out.println("Table create successfully");
             } else {
                 System.out.println("Table already exist");
             }
         } catch (SQLException | ClassNotFoundException e){
             System.out.println("Database error occurred: " + e.getMessage());
         }

         //initializing table columns
         serial_no.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(customerTable.getItems().indexOf(column.getValue()) + 1));
         customer_id.setCellValueFactory(cellData -> cellData.getValue().getCustomerIdProperty());
         customer_name.setCellValueFactory(cellData -> cellData.getValue().getCustomerNameProperty());
         contact_no.setCellValueFactory(cellData -> cellData.getValue().getContactNumberProperty());
         email.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());

         customers = CustomerHelper.getAllRecords();
         populateTable(customers);
         // setting customer id field as uneditable filed and applying grayed out css
         customer_id_field.setEditable(false);
         customer_id_field.setStyle("-fx-text-fill: gray;");
         //click on the items of the table and the data is displayed is the respective text fields
         customerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
             if(newValue != null){
                 //search_id.setText(newValue.getCustomerId());
                 customer_id_field.setText(newValue.getCustomerId());
                 customer_name_field.setText(newValue.getCustomerName());
                 contact_no_field.setText(newValue.getContactNumber());
                 email_field.setText(newValue.getEmail());
             }
         });
     }

    private void populateTable(ObservableList<Customer> customers) {
         customerTable.setItems(customers);
    }

    private void clearFields() {
         customer_id_field.clear();
         customer_name_field.clear();
         contact_no_field.clear();
         email_field.clear();
         search_id.clear();
    }

    @FXML
    void onSave(ActionEvent event) throws Exception {
        String name = customer_name_field.getText();
        String id = customer_id_field.getText();
        String contact = contact_no_field.getText();
        String email = email_field.getText();
        String customerId = GenericUtils.GenerateCustomerNo();
        System.out.println("Customer ID: " + customerId);

        try {
            if (!name.isEmpty() && !contact.isEmpty() && !email.isEmpty()) {
                int result = actions.createCustomer(customerId, name, contact, email);

                if (result == 1) {
                    dialogBoxUtility.showDialogBox(1);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }

                customers = CustomerHelper.getAllRecords();
                populateTable(customers);
            }
        } catch (Exception e) {
            //System.out.println("Error from mySql server "+ e.getMessage());
            dialogBoxUtility.showDialogBox(7);
            //e.printStackTrace();
        }

        clearFields();
    }

    @FXML
    void onUpdate(ActionEvent event) throws Exception {
        String customerId = customer_id_field.getText();
        String name = customer_name_field.getText();
        String contact = contact_no_field.getText();
        String email = email_field.getText();

        try {
            int result = 0;
            result = actions.updateCustomer(customerId, name, contact, email);

            if (result == 2) {
                dialogBoxUtility.showDialogBox(2);
                customers = CustomerHelper.getAllRecords();
                populateTable(customers);
            } else if(result == 0){
                dialogBoxUtility.showDialogBox(0);
            } else {
                dialogBoxUtility.showDialogBox(7);
            }
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }

        clearFields();
    }


    @FXML
    void onDelete(ActionEvent event) throws Exception{
        String deleteItemId = customer_id_field.getText();
        try{
            int result = actions.deleteCustomer(deleteItemId);
            if(result == 4) {
                dialogBoxUtility.showDialogBox(4);
            } else{
                dialogBoxUtility.showDialogBox(5);
            }

            customers = CustomerHelper.getAllRecords();
            populateTable(customers);
            System.out.println("Deleted Id: " + search_id);

        } catch(Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        clearFields();
    }

    @FXML
    void onSearch(ActionEvent event) throws Exception{
        String searchInput = search_id.getText();

        try{
            resultSet = actions.searchCustomer(searchInput);

            // need to change this logic of displaying result with respective fields this may cause problem when search result is more than one value

            if(resultSet.next()){
                customer_id_field.setText(resultSet.getString("customer_id"));
                customer_name_field.setText(resultSet.getString("customer_name"));
                contact_no_field.setText(resultSet.getString("contact_no"));
                email_field.setText(resultSet.getString("email"));
            } else if (!resultSet.isBeforeFirst()) {
                dialogBoxUtility.showDialogBox(3);
            }

            customers = CustomerHelper.getSearchedList(searchInput);
            populateTable(customers);

        } catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }

        clearFields();
    }

    @FXML
    void onClear() {
         clearFields();
    }
}

// time - 06:36:30