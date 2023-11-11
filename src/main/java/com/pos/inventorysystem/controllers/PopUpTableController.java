package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Product;
import com.pos.inventorysystem.actions.PopUpTableActions;
import com.pos.inventorysystem.helpers.PopUpTableHelper;
import com.pos.inventorysystem.utils.DialogBoxUtility;
import com.pos.inventorysystem.utils.TableUtility;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PopUpTableController {

    @FXML
    private TextField search_product_field_2;

    @FXML
    private Button addNCloseBtn;

    @FXML
    private StackPane searchResultTable;

    private final TableView<Product> productTable = new TableView<>();

    DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();

    private final PopUpTableActions actions = new PopUpTableActions();

    // Reference to the InvoiceController
    private InvoiceController invoiceController;

    private ObservableList<Product> tableData = null;

    public void setInvoiceController(InvoiceController controller) {
        this.invoiceController = controller;
    }

    @FXML
    public void initialize() {
        // create a table and initialize with data
        initializeTable();
    }

    @SuppressWarnings("unchecked")
    private void initializeTable() {
        TableColumn<Product, Integer> serial_no = new TableColumn<>("serial no".toUpperCase());
        TableColumn<Product, String> product_name = TableUtility.createTableColumn("product name".toUpperCase(), Product::getProductName);
        TableColumn<Product,String> barcode = TableUtility.createTableColumn("barcode".toUpperCase(), Product::getBarcode);
        TableColumn<Product, String> price = TableUtility.createTableColumn("price".toUpperCase(), Product::getPrice);
        TableColumn<Product, Integer> quantity = TableUtility.createTableColumn("qty".toUpperCase(), Product::getQuantity);

        serial_no.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(productTable.getItems().indexOf(cellData.getValue()) + 1));

        serial_no.setPrefWidth(80);
        product_name.setPrefWidth(180);
        barcode.setPrefWidth(100);
        price.setPrefWidth(60);
        quantity.setPrefWidth(50);

        productTable.getColumns().addAll(serial_no, product_name, barcode, price, quantity);
        searchResultTable.getChildren().add(productTable);
    }

    private void populateTableData(ObservableList<Product> data) throws SQLException {
        //populate table with data
        productTable.setItems(data);
    }

    @FXML
    void onAddProduct(ActionEvent event) {
        //Get the current stage (window)
        Stage stage = (Stage) addNCloseBtn.getScene().getWindow();
        ObservableList<Product> selectedItems = productTable.getSelectionModel().getSelectedItems();
        invoiceController.addToProductList(selectedItems);

        //close the stage
        stage.close();
    }

    @FXML
    void onProductSearch(ActionEvent event) {
        String input = search_product_field_2.getText().trim();
        searchProduct(input);
    }

    public void searchProduct(String searchInput) {
        try {
            tableData = PopUpTableHelper.getAllSearchList(searchInput);
            if(searchInput == null) {
                dialogBoxUtility.showDialogBox(7);
            } else if(tableData == null) {
                dialogBoxUtility.showDialogBox(3);
            } else {
                populateTableData(tableData);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
