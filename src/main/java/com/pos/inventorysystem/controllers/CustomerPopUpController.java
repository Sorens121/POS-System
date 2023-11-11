package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Customer;
import com.pos.inventorysystem.helpers.CustomerPopUpHelper;
import com.pos.inventorysystem.utils.TableUtility;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerPopUpController {
    @FXML
    private AnchorPane childLayout;

    @FXML
    private StackPane customerTableArea;

    @FXML
    private Button addButton;

    private final TableView<Customer> customerTable = new TableView<>();

    // Reference to the InvoiceController

    private BooleanProperty isTableEmpty = new SimpleBooleanProperty(true);

    private BooleanProperty isItemSelected = new SimpleBooleanProperty(false);

    private InvoiceController invoiceController;

    public void setInvoiceController(InvoiceController controller) {
        this.invoiceController = controller;
    }

    @FXML
    public void initialize() {
        // initialize customer table
        initializeTable();

        addButton.setDisable(true); // Initially the add button is disabled

        // create BooleanProperties for the selection model and table emptiness
        customerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isItemSelected.set(newValue != null);
        });

        // Add a listener to enable/disable the button based on conditions
        isItemSelected.addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(false);
        });


    }

    @SuppressWarnings("unchecked")
    private void initializeTable() {
        TableColumn<Customer, Integer> serial_no = new TableColumn<>("serial no".toUpperCase());
        TableColumn<Customer, String> customer_name = TableUtility.createTableColumn("customer name".toUpperCase(), Customer::getCustomerName);
        TableColumn<Customer, String> customer_id = TableUtility.createTableColumn("customer id".toUpperCase(), Customer::getCustomerId);
        TableColumn<Customer, String> contact = TableUtility.createTableColumn("contact".toUpperCase(), Customer::getContactNumber);
        TableColumn<Customer, String> email = TableUtility.createTableColumn("email".toUpperCase(), Customer::getEmail);

        serial_no.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(customerTable.getItems().indexOf(cellData.getValue()) + 1));

        serial_no.setPrefWidth(80);
        customer_name.setPrefWidth(200);
        customer_id.setPrefWidth(150);
        contact.setPrefWidth(150);
        email.setPrefWidth(200);

        customerTable.getColumns().addAll(serial_no, customer_name, customer_id, contact, email);

        customerTableArea.getChildren().add(customerTable);
    }

    @FXML
    void addCustomer(ActionEvent event) {
        // Get current stage (window)
        Stage stage = (Stage) addButton.getScene().getWindow();

        Customer selectedItem = customerTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            String id = selectedItem.getCustomerId();
            String name = selectedItem.getCustomerName();

            invoiceController.setSelectedData(id, name);
        }

        //close the stage
        stage.close();
    }

    // this method will be called by the invoice controller & also wil set the table data for customer search result
    public void searchCustomer(String searchInput) throws SQLException {
        try{
            ObservableList<Customer> customer = CustomerPopUpHelper.searchCustomerRecords(searchInput);
            //isCustomerFound = true;
            customerTable.setItems(customer);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
