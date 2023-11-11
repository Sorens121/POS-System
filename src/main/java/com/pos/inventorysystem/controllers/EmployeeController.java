package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Employee;
import com.pos.inventorysystem.actions.EmployeeActions;
import com.pos.inventorysystem.helpers.EmployeeHelper;
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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
    private StackPane tableArea;

    private final static String CREATE_TABLE_QUERY = "CREATE TABLE employee (employee_id VARCHAR(12) NOT NULL, employee_name VARCHAR(45), contact_no VARCHAR(10), email VARCHAR(45), PRIMARY KEY(employee_id), UNIQUE(contact_no, email))";

    private ObservableList<Employee> tableData = null;

    DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();
    EmployeeActions actions = new EmployeeActions();

    private final TableView<Employee> employeeTable = new TableView<>();

    @FXML
    public void initialize() throws SQLException {
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
        } catch (SQLException e){
            System.out.println("Database error occurred: " + e.getMessage());
        }

        //since id field is auto generated for disabling the field
        employee_id_field.setDisable(true);
        employee_id_field.setPromptText("This field is auto generated");
        employee_id_field.setStyle("-fx-prompt-text-fill: #1a1aff; -fx-background-color: #f4f4f4; -fx-border-width: 1px; -fx-border-color: #ccc;");

        tableData = EmployeeHelper.getAllRecords();
        initializeTable(tableData);
        populateTableData(tableData);

        //click on the items of the table and the data is displayed is the respective text fields
        employeeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                employee_id_field.setText(newValue.getEmployeeId());
                // setting customer id field as uneditable filed
                employee_id_field.setEditable(false);
                name_field.setText(newValue.getEmployeeName());
                contact_no_field.setText(newValue.getContactNo());
                email_field.setText(newValue.getEmail());
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initializeTable(ObservableList<Employee> data) {
        TableColumn<Employee, Integer> serial_no = new TableColumn<>("Serial No".toUpperCase());
        TableColumn<Employee, String> employee_id = TableUtility.createTableColumn("Employee Id".toUpperCase(), Employee::getEmployeeId);
        TableColumn<Employee, String> employee_name = TableUtility.createTableColumn("Employee Name".toUpperCase(), Employee::getEmployeeName);
        TableColumn<Employee, String> contact = TableUtility.createTableColumn("Contact No".toUpperCase(), Employee::getContactNo);
        TableColumn<Employee, String> email = TableUtility.createTableColumn("Email".toUpperCase(), Employee::getEmail);

        if(data != null) {
            serial_no.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(employeeTable.getItems().indexOf(cellData.getValue()) + 1 ));
        }

        serial_no.setPrefWidth(80);
        employee_id.setPrefWidth(150);
        employee_name.setPrefWidth(200);
        contact.setPrefWidth(150);
        email.setPrefWidth(200);

        employeeTable.getColumns().addAll(serial_no, employee_id, employee_name, contact, email);
        tableArea.getChildren().add(employeeTable);
    }

    private void populateTableData(ObservableList<Employee> data){
        //populate table with data
        employeeTable.setItems(data);
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
        search_field.clear();
    }


    @FXML
    void onSave(ActionEvent event) throws Exception {
        String name = name_field.getText().trim();
        String contact = contact_no_field.getText().trim();
        String email = email_field.getText().trim();
        String id = employee_id_field.getText().trim();

        if(id.isEmpty() || id.isBlank()) {
            id = GenericUtils.GenerateEmployeeNo();
        }

        System.out.println("Employee ID: " + id);
        String selectQuery = "SELECT * FROM employee WHERE employee_id = ?";

        try{
            //check for duplicates
            boolean checkDuplicate = GenericSQLHelper.checkDuplicate(selectQuery, id);
            if(name.isBlank() || contact.isBlank() || email.isBlank()){
                dialogBoxUtility.showDialogBox(7);
            }else if(checkDuplicate){// if true then only save data
                int result = actions.createNewRecord(id, name, contact, email);

                if (result == 1) {
                    dialogBoxUtility.showDialogBox(1);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }
            } else {
                dialogBoxUtility.showDialogBox(8);
            }

            tableData = EmployeeHelper.getAllRecords();
            populateTableData(tableData);

        } catch (Exception e) {
            System.out.println("Error from controller class: " + e.getMessage());
            dialogBoxUtility.showDialogBox(8);
        }
        clearFields();
    }

    @FXML
    void onUpdate(ActionEvent event) {
        String employeeId = employee_id_field.getText().trim();
        String name = name_field.getText().trim();
        String contact = contact_no_field.getText().trim();
        String email = email_field.getText().trim();

        List<String> params = List.of(name, contact, email);

        String query = "SELECT * FROM employee WHERE employee_id = ?";

        try{
            // need to check for unchanged values and null before running the queries
            boolean updateRequired = GenericSQLHelper.shouldUpdateRow(query, employeeId, params);
            int result = 0;

            if(name.isBlank() || contact.isBlank() || email.isBlank()){
                dialogBoxUtility.showDialogBox(7);
            } else if(updateRequired){
                result = actions.updateRecord(employeeId, name, contact, email);

                if(result == 2) {
                    dialogBoxUtility.showDialogBox(2);
                    tableData = EmployeeHelper.getAllRecords();
                    populateTableData(tableData);
                } else if(result == 0) {
                    dialogBoxUtility.showDialogBox(0);
                } else{
                    dialogBoxUtility.showDialogBox(7);
                }

            } else {
                dialogBoxUtility.showDialogBox(8);
            }
        } catch (Exception e) {
            System.out.println("Error from controller class: " + e.getMessage());
            e.printStackTrace();
        }
        clearFields();
    }

    @FXML
    void onDelete(ActionEvent event) {
        String deleteItemId = employee_id_field.getText();
        try{
            int result = actions.deleteRecord(deleteItemId);
            if(result == 4) {
                dialogBoxUtility.showDialogBox(4);
            } else{
                dialogBoxUtility.showDialogBox(5);
            }

            tableData = EmployeeHelper.getAllRecords();
            populateTableData(tableData);

        } catch(Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }

        clearFields();
    }

    @FXML
    void onSearch(ActionEvent event) {
        String searchInput = search_field.getText().trim();

        try{
            tableData = EmployeeHelper.getSearchedList(searchInput);
            populateTableData(tableData);
            if(tableData != null) {
                employee_id_field.setText(tableData.get(0).getEmployeeId());
                name_field.setText(tableData.get(0).getEmployeeName());
                contact_no_field.setText(tableData.get(0).getContactNo());
                email_field.setText(tableData.get(0).getEmail());
            } else {
                dialogBoxUtility.showDialogBox(3);
            }

        } catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
        clearFields();
    }
}
