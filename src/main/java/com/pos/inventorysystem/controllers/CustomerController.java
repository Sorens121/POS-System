package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Customer;
import com.pos.inventorysystem.actions.CustomerActions;
import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.CustomerHelper;
import com.pos.inventorysystem.utils.DialogBoxUtility;
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
    private TextField c_name;

    @FXML
    private TextField search_id;

    @FXML
    private TextField tp_number;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> Tp_number;

    @FXML
    private TableColumn<Customer, String> c_id;

    @FXML
    private TableColumn<Customer, String> customer_name;

//    @FXML
//    private Button deleteBtn;
//
//    @FXML
//    private Button saveBtn;
//
//    @FXML
//    private Button searchBtn;
//
//    @FXML
//    private Button updateBtn;

    private ObservableList<Customer> customers = null;
    private ResultSet resultSet = null;

    DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();
    CustomerActions actions = new CustomerActions();


     @FXML
     private void initialize() throws Exception{
         c_id.setCellValueFactory(cellData -> cellData.getValue().getCustomerIdProperty());
         customer_name.setCellValueFactory(cellData -> cellData.getValue().getCustomerNameProperty());
         Tp_number.setCellValueFactory(cellData -> cellData.getValue().getTpNumberProperty());
         customers = CustomerHelper.getAllRecords();
         populateTable(customers);

         //click on the items of the table and the data is displayed is the respective text fields
         customerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
             if(newValue != null){
                 search_id.setText(newValue.getCustomerId());
                 c_name.setText(newValue.getCustomerName());
                 tp_number.setText(newValue.getTpNumber());
             }
         });
     }

    private void populateTable(ObservableList<Customer> customers) {
         customerTable.setItems(customers);
    }

    @FXML
    void onSave(ActionEvent event) throws Exception {
        String name = c_name.getText();
        String tp = tp_number.getText();

        try {
            if (!name.isEmpty() && !tp.isEmpty()) {
                int result = actions.createCustomer(name, tp);
                if (result == 1) {
                    dialogBoxUtility.showDialogBox(1);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }

                customers = CustomerHelper.getAllRecords();
                populateTable(customers);

            } else {
                dialogBoxUtility.showDialogBox(7);
            }

        } catch (Exception e) {
            //System.out.println("Error from mySql server "+ e.getMessage());
            dialogBoxUtility.showDialogBox(8);
            //e.printStackTrace();
        }
    }

    @FXML
    void onUpdate(ActionEvent event) throws Exception{
        String id = search_id.getText();
        String name = c_name.getText();
        String tp = tp_number.getText();

        try{
            if (!name.isEmpty() || !tp.isEmpty()) {
                int result = actions.updateCustomer(id, name, tp);
                if (result == 2) {
                    dialogBoxUtility.showDialogBox(2);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }

                customers = CustomerHelper.getAllRecords();
                populateTable(customers);
            } else {
                dialogBoxUtility.showDialogBox(7);
            }

        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }


    @FXML
    void onDelete(ActionEvent event) throws Exception{
        String id = search_id.getText();
        try{
            int result = actions.deleteCustomer(id);
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
    }

    @FXML
    void searchInfo(ActionEvent event) throws Exception{
        String id = search_id.getText();

        try{
            resultSet = actions.searchCustomerById(id);
            if(resultSet.next()){
                c_name.setText(resultSet.getString("customer_name"));
                tp_number.setText(resultSet.getString("Tp_Number"));
            } else if (!resultSet.isBeforeFirst()) {
                dialogBoxUtility.showDialogBox(3);
            }

            customers = CustomerHelper.getSearchedList(id);
            populateTable(customers);

        } catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    @FXML
    void onSearch(ActionEvent event) {
        String name = c_name.getText();
        String tp = tp_number.getText();

        System.out.println("Name: "+name+",TP: "+tp);

        try{
            if (!name.isEmpty() || !tp.isEmpty()) {
                resultSet = actions.searchCustomerByDetails(name, tp);
                if (!resultSet.isBeforeFirst()) {
                    dialogBoxUtility.showDialogBox(3);
                } else {
                    dialogBoxUtility.showDialogBox(6);
                }
                customers = CustomerHelper.getSearchedListByDetails(name, tp);
                populateTable(customers);
            } else {
                dialogBoxUtility.showDialogBox(7);
            }


        } catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}

// time - 06:36:30