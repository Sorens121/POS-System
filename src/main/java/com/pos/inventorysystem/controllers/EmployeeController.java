package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Employee;
import com.pos.inventorysystem.actions.EmployeeActions;
import com.pos.inventorysystem.helpers.EmployeeHelper;
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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeController {

    @FXML
    private TextField search_field;

    @FXML
    private TextField employee_id_field;

    @FXML
    private TextField contact_no_field;

    @FXML
    private TextField email_field;

    @FXML
    private TextField name_field;

    @FXML
    private TableView<Employee> employee_table;

    @FXML
    private TableColumn<Employee, Integer> serial_no;

    @FXML
    private TableColumn<Employee, String> employee_name;

    @FXML
    private TableColumn<Employee, String> employee_id;

    @FXML
    private TableColumn<Employee, String> contact_no;

    @FXML
    private TableColumn<Employee, String> email;

    private final static String CREATE_TABLE_QUERY = "CREATE TABLE employee (employee_id VARCHAR(12) NOT NULL, employee_name VARCHAR(45), contact_no VARCHAR(10), email VARCHAR(45), PRIMARY KEY(employee_id), UNIQUE(contact_no, email))";

    private ObservableList<Employee> employees = null;

    private ResultSet resultSet = null;

    DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();
    EmployeeActions actions = new EmployeeActions();


    @FXML
    public void initialize() throws ClassNotFoundException, SQLException {
        ConfigFileManager configFileManager = new ConfigFileManager();
        try {
            //check if table exists
            boolean tableCheckFlag = Boolean.parseBoolean(configFileManager.getProperty("employee_table.flag"));
            //System.out.println("From Database: " + tableExists);

            if(!tableCheckFlag) {
                TableUtility.createTable(CREATE_TABLE_QUERY);
                configFileManager.setProperty("employee_table.flag", "true");
                System.out.println("Table create successfully");
            } else {
                System.out.println("Table already exist");
            }
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Database error occurred: " + e.getMessage());
        }

        // setting up the employees table
        serial_no.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(employee_table.getItems().indexOf(column.getValue()) + 1));
        employee_id.setCellValueFactory(cellData -> cellData.getValue().getEmployeeIdProperty());
        employee_name.setCellValueFactory(cellData -> cellData.getValue().getEmployeeNameProperty());
        contact_no.setCellValueFactory(cellData -> cellData.getValue().getContactNoProperty());
        email.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
        employees = EmployeeHelper.getAllRecords();
        populateTable(employees);

        //click on the items of the table and the data is displayed is the respective text fields
        employee_table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                //search_field.setText(newValue.getEmployeeId());
                employee_id_field.setText(newValue.getEmployeeId());
                employee_id_field.setEditable(false);
                employee_id_field.setStyle("-fx-text-fill: gray; -fx-background-color: #f4f4f4; -fx-border-width: 1px; -fx-border-color: #ccc;");
                name_field.setText(newValue.getEmployeeName());
                contact_no_field.setText(newValue.getContactNo());
                email_field.setText(newValue.getEmail());
            }
        });

    }

    private void populateTable(ObservableList<Employee> employees) {
        employee_table.setItems(employees);
    }

    @FXML
    void onClear() {
        clearFields();
    }

    private void clearFields() {
        employee_id_field.clear();
        name_field.clear();
        contact_no_field.clear();
        email_field.clear();
    }


    @FXML
    void onSave(ActionEvent event) throws IOException {
        String name = name_field.getText();
        String contact = contact_no_field.getText();
        String email = email_field.getText();
        String employeeId = GenericUtils.GenerateEmployeeNo();

        try{
            if(!name.isEmpty() && !contact.isEmpty() && !email.isEmpty()) {
                int result = actions.createNewEmployee(employeeId, name, contact, email);
                if(result == 1) {
                    dialogBoxUtility.showDialogBox(1);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }
            }

            employees = EmployeeHelper.getAllRecords();
            populateTable(employees);

        } catch (Exception e) {
            System.out.println("Error from controller class: " + e.getMessage());
            dialogBoxUtility.showDialogBox(8);
        }
        clearFields();
    }

    @FXML
    void onUpdate(ActionEvent event) {
        String employeeId = employee_id_field.getText();
        String name = name_field.getText();
        String contact = contact_no_field.getText();
        String email = email_field.getText();

        try{
            int result = actions.updateEmployee(employeeId, name, contact, email);

            if(result == 2) {
                dialogBoxUtility.showDialogBox(2);
                employees = EmployeeHelper.getAllRecords();
                populateTable(employees);
            } else if(result == 0) {
                dialogBoxUtility.showDialogBox(0);
            } else{
                dialogBoxUtility.showDialogBox(7);
            }
        } catch (Exception e) {
            System.out.println("Error from controller class: " + e.getMessage());
            e.printStackTrace();
        }
        clearFields();
    }

    @FXML
    void onDelete(ActionEvent event) {
        String deleteItemId = search_field.getText();
        try{
            int result = actions.deleteEmployee(deleteItemId);
            if(result == 4) {
                dialogBoxUtility.showDialogBox(4);
            } else{
                dialogBoxUtility.showDialogBox(5);
            }

            employees = EmployeeHelper.getAllRecords();
            populateTable(employees);

        } catch(Exception e) {
            System.out.println("Error from controller class: " + e.getMessage());
            e.printStackTrace();
        }

        clearFields();
    }

    @FXML
    void onSearch(ActionEvent event) {
        String searchInput = search_field.getText();
        //System.out.println("Name: "+name+",TP: "+tp);

        try{
            resultSet = actions.searchEmployee(searchInput);

            if(resultSet.next()) {
                employee_id_field.setText(resultSet.getString("employee_id"));
                name_field.setText(resultSet.getString("employee_name"));
                contact_no_field.setText(resultSet.getString("contact_no"));
                email_field.setText(resultSet.getString("email"));
            } else if(!resultSet.isBeforeFirst()) {
                dialogBoxUtility.showDialogBox(3);
            }

            employees = EmployeeHelper.getSearchedList(searchInput);
            populateTable(employees);

        } catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
