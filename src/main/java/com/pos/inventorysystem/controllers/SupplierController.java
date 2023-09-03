package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Supplier;
import com.pos.inventorysystem.actions.SupplierActions;
import com.pos.inventorysystem.helpers.SupplierHelper;
import com.pos.inventorysystem.utils.DialogBoxUtility;
import com.pos.inventorysystem.utils.TableUtility;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierController {
    @FXML
    private TextField s_id;

    @FXML
    private TextField s_name;

    @FXML
    private TextField s_tp_number;

    @FXML
    private TableColumn<Supplier, String> supplier_id;

    @FXML
    private TableColumn<Supplier, String> supplier_name;

    @FXML
    private TableView<Supplier> supplier_table;

    @FXML
    private TableColumn<Supplier, String> supplier_tp;

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE supplier (sid INT PRIMARY KEY AUTO_INCREMENT, supplier_name VARCHAR(45), supplier_Tp_Number VARCHAR(10), UNIQUE(supplier_Tp_Number))";
    private ObservableList<Supplier> suppliers = null;
    private ResultSet resultSet = null;

    DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();
    SupplierActions actions = new SupplierActions();

   @FXML
   public void initialize() throws SQLException, ClassNotFoundException {
       try {
           //check if table exists
           boolean tableExists = TableUtility.checkTableExists("suppliers");
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

       // setting up the suppliers table
       supplier_id.setCellValueFactory(cellData -> cellData.getValue().getSupplierIdProperty());
       supplier_name.setCellValueFactory(cellData -> cellData.getValue().getSupplierNameProperty());
       supplier_tp.setCellValueFactory(cellData -> cellData.getValue().getSupplierTpNoProperty());
       suppliers = SupplierHelper.getAllRecords();
       populateTable(suppliers);

       //click on the items of the table and the data is displayed is the respective text fields
       supplier_table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           if(newValue != null) {
               s_id.setText(newValue.getSupplierId());
               s_name.setText(newValue.getSupplierName());
               s_tp_number.setText(newValue.getSupplierTpNo());
           }
       });
   }

    private void populateTable(ObservableList<Supplier> suppliers) {
       supplier_table.setItems(suppliers);
    }

    @FXML
    void onSave(ActionEvent event) throws Exception{
        String name = s_name.getText();
        String tp = s_tp_number.getText();

        try{
            if(!name.isEmpty() && !tp.isEmpty()) {
                int result = actions.createNewSupplier(name, tp);
                if(result == 1) {
                    dialogBoxUtility.showDialogBox(1);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }
            }

            suppliers = SupplierHelper.getAllRecords();
            populateTable(suppliers);

        } catch (Exception e) {
            System.out.println("Error from controller class: " + e.getMessage());
            dialogBoxUtility.showDialogBox(8);
        }
    }

    @FXML
    void onSearch(ActionEvent event) {
        String name = s_name.getText();
        String tp = s_tp_number.getText();

        //System.out.println("Name: "+name+",TP: "+tp);

        try{
            if (!name.isEmpty() || !tp.isEmpty()) {
                resultSet = actions.searchSupplierByDetails(name, tp);
                if (!resultSet.isBeforeFirst()) {
                    dialogBoxUtility.showDialogBox(3);
                } else {
                    dialogBoxUtility.showDialogBox(6);
                }
                suppliers = SupplierHelper.getSearchedListByDetails(name, tp);
                populateTable(suppliers);

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
        String id = s_id.getText();

        try{
            resultSet = actions.searchSupplierById(id);
            if(resultSet.next()){
                s_name.setText(resultSet.getString("supplier_name"));
                s_tp_number.setText(resultSet.getString("supplier_Tp_Number"));
            } else if (!resultSet.isBeforeFirst()) {
                dialogBoxUtility.showDialogBox(3);
            }

            suppliers = SupplierHelper.getSearchedList(id);
            populateTable(suppliers);

        } catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    @FXML
    void onUpdate(ActionEvent event) {
        String id = s_id.getText();
        String name = s_name.getText();
        String tp = s_tp_number.getText();

        try{
            if (!name.isEmpty() || !tp.isEmpty()) {
                int result = actions.updateSupplier(id, name, tp);
                if (result == 2) {
                    dialogBoxUtility.showDialogBox(2);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }

                suppliers = SupplierHelper.getAllRecords();
                populateTable(suppliers);

            } else {
                dialogBoxUtility.showDialogBox(7);
            }

        } catch (Exception e) {
            System.out.println("Error from controller class: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onDelete(ActionEvent event) throws Exception{
        String id = s_id.getText();
        try{
            int result = actions.deleteSupplier(id);
            if(result == 4) {
                dialogBoxUtility.showDialogBox(4);
            } else{
                dialogBoxUtility.showDialogBox(5);
            }

            suppliers = SupplierHelper.getAllRecords();
            populateTable(suppliers);

        } catch(Exception e) {
            System.out.println("Error from controller class: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
