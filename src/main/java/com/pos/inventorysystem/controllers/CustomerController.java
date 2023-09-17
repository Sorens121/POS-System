package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Customer;
import com.pos.inventorysystem.actions.CustomerActions;
import com.pos.inventorysystem.helpers.CustomerHelper;
import com.pos.inventorysystem.helpers.GenericSQLHelper;
import com.pos.inventorysystem.utils.ConfigFileManager;
import com.pos.inventorysystem.utils.DialogBoxUtility;
import com.pos.inventorysystem.utils.GenericUtils;
import com.pos.inventorysystem.utils.TableUtility;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
    private StackPane tableArea;

    private ResultSet resultSet = null;

    DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();
    CustomerActions actions = new CustomerActions();

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE customer (customer_id VARCHAR(12) NOT NULL, customer_name VARCHAR(45), contact_no VARCHAR(10), email VARCHAR(45), PRIMARY KEY(customer_id), UNIQUE(contact_no, email))";

    private final TableView<Customer> customerTable = new TableView<>();

     @FXML
     private void initialize() throws SQLException, ClassNotFoundException{
         ConfigFileManager configFileManager = new ConfigFileManager();

         try{
             // CHECK TABLE FLAG FROM CONFIG FILE
             boolean tableCheckFlag = Boolean.parseBoolean(configFileManager.getProperty("customer_table.flag"));
             //System.out.println("Customer Table Flag " + tableCheckFlag);

             if(!tableCheckFlag) {
                 // IF FALSE THEN CREATE TABLE AND UPDATE FLAG
                 TableUtility.createTable(CREATE_TABLE_QUERY);
                 configFileManager.setProperty("customer_table.flag", "true");
                 System.out.println("Table create successfully");
             } else {
                 System.out.println("Table already exist");
             }

         } catch (SQLException | ClassNotFoundException e){
             System.out.println("Database error occurred: " + e.getMessage());
         }

         //Since the employee ID will auto generated so the field is non-editable
         customer_id_field.setDisable(true);
         customer_id_field.setPromptText("This field is auto generated");
         customer_id_field.setStyle("-fx-prompt-text-fill: #1a1aff; -fx-background-color: #f4f4f4; -fx-border-width: 1px; -fx-border-color: #ccc;");

         resultSet = CustomerHelper.getAllRecords();
         initializeTable(resultSet);
         populateTableData(resultSet);

         //click on the items of the table and the data is displayed is the respective text fields
         customerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
             if(newValue != null){
                 //search_id.setText(newValue.getCustomerId());
                 customer_id_field.setText(newValue.getCustomerId());
                 // setting customer id field as uneditable filed
                 customer_id_field.setEditable(false);
                 customer_name_field.setText(newValue.getCustomerName());
                 contact_no_field.setText(newValue.getContactNumber());
                 email_field.setText(newValue.getEmail());
             }
         });
     }

     // initializing the table for first time i.e. it is called only once
    @SuppressWarnings("unchecked")
     public void initializeTable(ResultSet resultSet) {
        TableColumn<Customer, Integer> serial_no = new TableColumn<>("Serial No".toUpperCase());
        TableColumn<Customer, String> customer_id = TableUtility.createTableColumn("Customer Id".toUpperCase(), Customer::getCustomerId);
        TableColumn<Customer, String> customer_name = TableUtility.createTableColumn("customer name".toUpperCase(), Customer::getCustomerName);
        TableColumn<Customer, String> contact = TableUtility.createTableColumn("contact".toUpperCase(), Customer::getContactNumber);
        TableColumn<Customer, String> email = TableUtility.createTableColumn("email".toUpperCase(), Customer::getEmail);

        if(resultSet!= null){
            serial_no.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(customerTable.getItems().indexOf(column.getValue()) + 1));
        }

        serial_no.setPrefWidth(80);
        customer_id.setPrefWidth(150);
        customer_name.setPrefWidth(200);
        contact.setPrefWidth(150);
        email.setPrefWidth(200);

        customerTable.getColumns().addAll(serial_no, customer_id, customer_name, contact, email);
        tableArea.getChildren().add(customerTable);
     }

     // populate table with data
    public void populateTableData(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        // populate table with data
        try {
            ObservableList<Customer> customers = CustomerHelper.getAllCustomerRecords(resultSet);
            customerTable.setItems(customers);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClear() {
        clearFields();
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

        if(id.isEmpty() || id.isBlank()){
            id = GenericUtils.GenerateCustomerNo();
        }

        //System.out.println("Customer ID: " + id);
        String selectQuery = "SELECT * FROM customer WHERE customer_id = ?";

        try {
            // check duplicates before creating a new entry
            boolean checkDuplicate = GenericSQLHelper.checkDuplicate(selectQuery, id);
            if (name.isBlank() || contact.isBlank() || email.isBlank()) {
                dialogBoxUtility.showDialogBox(7);
            } else if (checkDuplicate) {// if false then only insert new customer
                int result = actions.createCustomer(id, name, contact, email);

                if (result == 1) {
                    dialogBoxUtility.showDialogBox(1);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }
            } else {
                dialogBoxUtility.showDialogBox(8);
            }

            resultSet = CustomerHelper.getAllRecords();
            populateTableData(resultSet);

        } catch (Exception e) {
            System.out.println("Error from mySql server " + e.getMessage());
            dialogBoxUtility.showDialogBox(7);
            //e.printStackTrace();
        }
        clearFields();
    }

    @FXML
    void onUpdate(ActionEvent event) throws Exception {
        String customerId = customer_id_field.getText().trim();
        String name = customer_name_field.getText().trim();
        String contact = contact_no_field.getText().trim();
        String email = email_field.getText().trim();

        List<String> params = List.of(name, contact, email);

        String query = "SELECT * FROM customer WHERE customer_id = ?";

        try {
            boolean updateRequired = GenericSQLHelper.shouldUpdateRow(query, customerId, params);
            int result = 0;

            if(name.isBlank() || contact.isBlank() || email.isBlank()){
                dialogBoxUtility.showDialogBox(7);
            } else if(updateRequired) {
                // need to check for unchanged values and null before running the queries
                result = actions.updateCustomer(customerId, name, contact, email);

                if (result == 2) {
                    dialogBoxUtility.showDialogBox(2);
                    resultSet = CustomerHelper.getAllRecords();
                    populateTableData(resultSet);
                } else if(result == 0){
                    dialogBoxUtility.showDialogBox(0);
                } else {
                    dialogBoxUtility.showDialogBox(7);
                }
            } else {
                dialogBoxUtility.showDialogBox(8);
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

            resultSet = CustomerHelper.getAllRecords();
            populateTableData(resultSet);

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

            if(resultSet.next()) {
                customer_id_field.setText(resultSet.getString("customer_id"));
                customer_name_field.setText(resultSet.getString("customer_name"));
                contact_no_field.setText(resultSet.getString("contact_no"));
                email_field.setText(resultSet.getString("email"));
            } else if(!resultSet.isBeforeFirst()) {
                dialogBoxUtility.showDialogBox(3);
            }

            ResultSet temp = CustomerHelper.getSearchedList(searchInput);
            populateTableData(temp);

        } catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }

        clearFields();
    }
}

// time - 06:36:30