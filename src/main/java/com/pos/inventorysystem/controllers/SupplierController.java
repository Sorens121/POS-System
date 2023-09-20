package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Supplier;
import com.pos.inventorysystem.actions.SupplierActions;
import com.pos.inventorysystem.helpers.GenericSQLHelper;
import com.pos.inventorysystem.helpers.SupplierHelper;
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

public class SupplierController {
    @FXML
    private TextField company_name_field;

    @FXML
    private TextField contact_no_field;

    @FXML
    private TextField email_field;

    @FXML
    private TextField name_field;

    @FXML
    private TextField search_field;

    @FXML
    private TextField supplier_id_field;

    @FXML
    private StackPane tableArea;

    private final TableView<Supplier> supplierTable = new TableView<>();

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE supplier (supplier_id VARCHAR(10) NOT NULL, supplier_name VARCHAR(45), contact VARCHAR(10), email VARCHAR(45), company_name VARCHAR(45), PRIMARY KEY (supplier_id), UNIQUE(supplier_id))";
    private ObservableList<Supplier> suppliers = null;
    private ResultSet resultSet = null;

    DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();
    SupplierActions actions = new SupplierActions();

   @FXML
   public void initialize() throws SQLException, ClassNotFoundException {
       ConfigFileManager configFileManager = new ConfigFileManager();
       try {
           //check if table exists
           boolean tableCheckFlag = Boolean.parseBoolean(configFileManager.getProperty("supplier_table.flag"));
           //System.out.println("From Database: " + tableExists);

           if(!tableCheckFlag) {
               TableUtility.createTable(CREATE_TABLE_QUERY);
               configFileManager.setProperty("supplier_table.flag", "true");
               System.out.println("Table create successfully");
           } else {
               System.out.println("Table already exist");
           }
       } catch (SQLException | ClassNotFoundException e){
           System.out.println("Database error occurred: " + e.getMessage());
       }

       // setting up the suppliers table
       supplier_id_field.setDisable(true);
       supplier_id_field.setPromptText("This field is auto generated");
       supplier_id_field.setStyle("-fx-prompt-text-fill: #1a1aff; -fx-background-color: #f4f4f4; -fx-border-width: 1px; -fx-border-color: #ccc;");

       resultSet = SupplierHelper.getAllRecords();
       initializeTable(resultSet);
       populateTableData(resultSet);

       //click on the table rows and the values are displayed in their respective fields
       supplierTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           if(newValue != null) {
               supplier_id_field.setText(newValue.getSupplierId());
               name_field.setText(newValue.getSupplierName());
               contact_no_field.setText(newValue.getSupplierContactNo());
               email_field.setText(newValue.getSupplierEmail());
               company_name_field.setText(newValue.getCompanyName());
           }
       });
   }

   @SuppressWarnings("unchecked")
   public void initializeTable(ResultSet resultSet) {
       TableColumn<Supplier, Integer> serial_no = new TableColumn<>("Serial No".toUpperCase());
       TableColumn<Supplier, String> supplier_id = TableUtility.createTableColumn("Supplier Id".toUpperCase(), Supplier::getSupplierId);
       TableColumn<Supplier, String> supplier_name = TableUtility.createTableColumn("supplier name".toUpperCase(), Supplier::getSupplierName);
       TableColumn<Supplier, String> contact = TableUtility.createTableColumn("contact".toUpperCase(), Supplier::getSupplierContactNo);
       TableColumn<Supplier, String> email = TableUtility.createTableColumn("email".toUpperCase(), Supplier::getSupplierEmail);
       TableColumn<Supplier, String> company_name = TableUtility.createTableColumn("company".toUpperCase(), Supplier::getCompanyName);

       if(resultSet != null) { // setting up the serial_no column
           serial_no.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(supplierTable.getItems().indexOf(cellData.getValue()) + 1));
       }

       serial_no.setPrefWidth(80);
       supplier_id.setPrefWidth(150);
       supplier_name.setPrefWidth(200);
       contact.setPrefWidth(150);
       email.setPrefWidth(200);
       company_name.setPrefWidth(200);

       supplierTable.getColumns().addAll(serial_no, supplier_name, supplier_id, contact, email, company_name);
       tableArea.getChildren().add(supplierTable);
   }

    private void populateTableData(ResultSet resultSet) throws SQLException {
       //populating table with data
        try {
            ObservableList<Supplier> suppliers = SupplierHelper.getAllSupplierRecords(resultSet);
            supplierTable.setItems(suppliers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClear() {
       clearFields();
    }

    private void clearFields() {
       supplier_id_field.clear();
       name_field.clear();
       contact_no_field.clear();
       email_field.clear();
       company_name_field.clear();
    }

    @FXML
    void onSave(ActionEvent event) throws Exception{
        String supplierId = supplier_id_field.getText().trim();
        String name = name_field.getText().trim();
        String contact = contact_no_field.getText().trim();
        String email = email_field.getText().trim();
        String company = company_name_field.getText().trim();

        // generating supplier id
        if (supplierId.isBlank() || supplierId.isEmpty()){
            supplierId = GenericUtils.GenerateSupplierId();
        }

        String query = "SELECT * FROM supplier WHERE supplier_id = ?";

        try{ // check for duplicate entry before saving bew data
            boolean checkDuplicate = GenericSQLHelper.checkDuplicate(query, supplierId);

            if(name.isBlank() || contact.isBlank() || email.isBlank() || company.isBlank()) {
                dialogBoxUtility.showDialogBox(7);
            } else if(checkDuplicate) {
                int result = actions.createNewSupplier(supplierId, name, contact, email, company);
                if(result == 1) {
                    dialogBoxUtility.showDialogBox(1);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }
            } else {
                dialogBoxUtility.showDialogBox(8);
            }

            resultSet = SupplierHelper.getAllRecords();
            populateTableData(resultSet);

        } catch (Exception e) {
            System.out.println("Error from controller class: " + e.getMessage());
            dialogBoxUtility.showDialogBox(8);
        }

        clearFields();
    }

    @FXML
    void onSearch(ActionEvent event) {
        String searchInput = search_field.getText().trim();

            try {
                resultSet = actions.searchSupplier(searchInput);

                if(resultSet.next()) {
                    supplier_id_field.setText(resultSet.getString("supplier_id"));
                    name_field.setText(resultSet.getString("supplier_name"));
                    contact_no_field.setText(resultSet.getString("contact"));
                    email_field.setText(resultSet.getString("email"));
                    company_name_field.setText(resultSet.getString("company_name"));
                } else if(!resultSet.isBeforeFirst()) {
                    dialogBoxUtility.showDialogBox(3);
                }

                ResultSet temp = SupplierHelper.getSearchedList(searchInput);
                populateTableData(temp);

            } catch (Exception e) {
                System.out.println("Error");
                e.printStackTrace();
            }

            clearFields();
    }


    @FXML
    void onUpdate(ActionEvent event) {
        String supplierId = supplier_id_field.getText().trim();
        String name = name_field.getText().trim();
        String contact = contact_no_field.getText().trim();
        String email = email_field.getText().trim();
        String company = company_name_field.getText().trim();

        List<String> params = List.of(name, contact, email, company);

        String query = "SELECT * FROM supplier WHERE supplier_id = ?";

        try{
            boolean updateRequired = GenericSQLHelper.shouldUpdateRow(query, supplierId, params);
            int result = 0;

            if (name.isBlank() || contact.isBlank() || email.isBlank() || company.isBlank()) {
                dialogBoxUtility.showDialogBox(7);
            } else if (updateRequired) {
                result = actions.updateSupplier(supplierId, name, contact, email, company);

                if (result == 2) {
                    dialogBoxUtility.showDialogBox(2);
                    resultSet = SupplierHelper.getAllRecords();
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
            System.out.println("Error from controller class: " + e.getMessage());
            e.printStackTrace();
        }

        clearFields();
    }

    @FXML
    void onDelete(ActionEvent event) throws Exception{
        String deleteItem = supplier_id_field.getText().trim();

        try{
            int result = actions.deleteSupplier(deleteItem);
            if(result == 4) {
                dialogBoxUtility.showDialogBox(4);
            } else{
                dialogBoxUtility.showDialogBox(5);
            }

            resultSet = SupplierHelper.getAllRecords();
            populateTableData(resultSet);

        } catch(Exception e) {
            System.out.println("Error from controller class: " + e.getMessage());
            e.printStackTrace();
        }

        clearFields();
    }

}
