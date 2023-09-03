package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Employee;
import com.pos.inventorysystem.actions.EmployeeActions;
import com.pos.inventorysystem.helpers.EmployeeHelper;
import com.pos.inventorysystem.utils.DialogBoxUtility;
import com.pos.inventorysystem.utils.TableUtility;
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
    private TableColumn<Employee, String> e_Tp_Number;

    @FXML
    private TableColumn<Employee, String> e_id;

    @FXML
    private TextField e_name;

    @FXML
    private TextField e_tp_number;

    @FXML
    private TableColumn<Employee, String> employee_name;

    @FXML
    private TableView<Employee> employee_table;

    @FXML
    private TextField search_id;

    private static String CREATE_TABLE_QUERY = "CREATE TABLE employee (eid INT PRIMARY KEY AUTO_INCREMENT, employee_name VARCHAR(45), employee_Tp_Number VARCHAR(10), UNIQUE(employee_Tp_Number))";
    private ObservableList<Employee> employees = null;
    private ResultSet resultSet = null;

    DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();
    EmployeeActions actions = new EmployeeActions();


    @FXML
    public void initialize() throws ClassNotFoundException, SQLException {
        try {
            //check if table exists
            boolean tableExists = TableUtility.checkTableExists("employee");
            //System.out.println("From Database: " + tableExists);

            // If the table doesn't exist, create it
            if(!tableExists) {
                TableUtility.createTable(CREATE_TABLE_QUERY);
                System.out.println("Table create successfully");
            } else {
                System.out.println("Table already exist");
            }
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Database error occurred: " + e.getMessage());
        }

        // setting up the employees table
        e_id.setCellValueFactory(cellData -> cellData.getValue().getEmployeeIdProperty());
        employee_name.setCellValueFactory(cellData -> cellData.getValue().getEmployeeNameProperty());
        e_Tp_Number.setCellValueFactory(cellData -> cellData.getValue().getEmployeeTpNumberProperty());
        employees = EmployeeHelper.getAllRecords();
        populateTable(employees);

        //click on the items of the table and the data is displayed is the respective text fields
        employee_table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                search_id.setText(newValue.getEmployeeId());
                e_name.setText(newValue.getEmployeeName());
                e_tp_number.setText(newValue.getEmployeeTpNumber());
            }
        });

    }

    private void populateTable(ObservableList<Employee> employees) {
        employee_table.setItems(employees);
    }


    @FXML
    void onSave(ActionEvent event) throws IOException {
        String name = e_name.getText();
        String tp = e_tp_number.getText();

        try{
            if(!name.isEmpty() && !tp.isEmpty()) {
                int result = actions.createNewEmployee(name, tp);
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
    }

    @FXML
    void onUpdate(ActionEvent event) {
        String id = search_id.getText();
        String name = e_name.getText();
        String tp = e_tp_number.getText();

        try{
            if (!name.isEmpty() || !tp.isEmpty()) {
                int result = actions.updateEmployee(id, name, tp);
                if (result == 2) {
                    dialogBoxUtility.showDialogBox(2);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }

                employees = EmployeeHelper.getAllRecords();
                populateTable(employees);

            } else {
                dialogBoxUtility.showDialogBox(7);
            }

        } catch (Exception e) {
            System.out.println("Error from controller class: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onDelete(ActionEvent event) {
        String id = search_id.getText();
        try{
            int result = actions.deleteEmployee(id);
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
    }

    @FXML
    void onSearch(ActionEvent event) {
        String name = e_name.getText();
        String tp = e_tp_number.getText();

        //System.out.println("Name: "+name+",TP: "+tp);

        try{
            if (!name.isEmpty() || !tp.isEmpty()) {
                resultSet = actions.searchEmployeeByDetails(name, tp);
                if (!resultSet.isBeforeFirst()) {
                    dialogBoxUtility.showDialogBox(3);
                } else {
                    dialogBoxUtility.showDialogBox(6);
                }
                employees = EmployeeHelper.getSearchedListByDetails(name, tp);
                populateTable(employees);

            } else {
                dialogBoxUtility.showDialogBox(7);
            }


        } catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    @FXML
    void onSearchInfo(ActionEvent event) {
        String id = search_id.getText();

        try{
            resultSet = actions.searchEmployeeById(id);
            if(resultSet.next()){
                e_name.setText(resultSet.getString("employee_name"));
                e_tp_number.setText(resultSet.getString("employee_Tp_Number"));
            } else if (!resultSet.isBeforeFirst()) {
                dialogBoxUtility.showDialogBox(3);
            }

            employees = EmployeeHelper.getSearchedList(id);
            populateTable(employees);

        } catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
