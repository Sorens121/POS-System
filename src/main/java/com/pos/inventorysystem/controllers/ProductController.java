package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Product;
import com.pos.inventorysystem.actions.ProductActions;
import com.pos.inventorysystem.helpers.GenericSQLHelper;
import com.pos.inventorysystem.helpers.ProductHelper;
import com.pos.inventorysystem.utils.ConfigFileManager;
import com.pos.inventorysystem.utils.DialogBoxUtility;
import com.pos.inventorysystem.utils.TableUtility;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductController {
    @FXML
    private TextField barcode_field;

    @FXML
    private TextField p_name;

    @FXML
    private TextField price_field;

    @FXML
    private TextField quantity_field;

    @FXML
    private TextField userInput;

    @FXML
    private TextField supplierId_field;

    @FXML
    private StackPane tableArea;


    private ObservableList<Product> products = null;
    private ResultSet resultSet = null;

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE product (barcode VARCHAR(12) NOT NULL, product_name VARCHAR(45), price VARCHAR(5) DEFAULT 0, quantity VARCHAR(5) DEFAULT 0, supplier_id VARCHAR(10) DEFAULT NULL, PRIMARY KEY(barcode), UNIQUE (barcode))";

    private final TableView<Product> productTable = new TableView<>();

    DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();
    ProductActions actions = new ProductActions();

    @FXML
    private void initialize() throws ClassNotFoundException, SQLException {
        ConfigFileManager configFileManager = new ConfigFileManager();
        try{
            //check if table exist
            boolean tableCheckFlag = Boolean.parseBoolean(configFileManager.getProperty("product_table.flag"));

            if(!tableCheckFlag) {
                TableUtility.createTable(CREATE_TABLE_QUERY);
                configFileManager.setProperty("product_table.flag", "true");
                System.out.println("Table created successfully");
            } else {
                System.out.println("Table already exist");
            }
        } catch(SQLException | ClassNotFoundException e) {
            System.out.println("Database error occurred: " + e.getMessage());
        }

        barcode_field.setPromptText("Paste here the generated barcode");
        barcode_field.setStyle("-fx-prompt-text-fill: #1a1aff;");

        //setting up products table
        resultSet = ProductHelper.getAllRecords();
        initializeTable(resultSet);
        populateTableData(resultSet);

        //System.out.print("Table data: " + product_name + barcode + price + quantity + supplier_id);

        //display details in their respective fields when items are selected from the table
        productTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                p_name.setText(newValue.getProductName());
                barcode_field.setText(newValue.getBarcode());
                barcode_field.setEditable(false);
                barcode_field.setStyle("-fx-text-fill: #1a1aff; -fx-background-color: #f4f4f4; -fx-border-width: 1px; -fx-border-color: #ccc;");
                price_field.setText(newValue.getPrice());
                quantity_field.setText(newValue.getQuantity());
                supplierId_field.setText(newValue.getSupplierId());
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initializeTable(ResultSet resultSet) {
        TableColumn<Product, Integer> serial_no = new TableColumn<>("Serial No".toUpperCase());
        TableColumn<Product, String> product_id= TableUtility.createTableColumn("Barcode".toUpperCase(), Product::getBarcode);
        TableColumn<Product, String> product_name = TableUtility.createTableColumn("product name".toUpperCase(), Product::getProductName);
        TableColumn<Product, String> price = TableUtility.createTableColumn("price".toUpperCase(), Product::getPrice);
        TableColumn<Product, String> quantity = TableUtility.createTableColumn("quantity".toUpperCase(), Product::getQuantity);
        TableColumn<Product, String> supplier_id = TableUtility.createTableColumn("supplier id".toUpperCase(), Product::getSupplierId);

        if(resultSet != null) {
            serial_no.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(productTable.getItems().indexOf(cellData.getValue()) + 1));
        }

        serial_no.setPrefWidth(80);
        product_id.setPrefWidth(150);
        product_name.setPrefWidth(200);
        price.setPrefWidth(50);
        quantity.setPrefWidth(80);
        supplier_id.setPrefWidth(150);

        productTable.getColumns().addAll(serial_no, product_name, product_id, price, quantity, supplier_id);
        tableArea.getChildren().add(productTable);
    }


    private void populateTableData(ResultSet resultSet) throws SQLException, ClassNotFoundException{
        try{
            ObservableList<Product> products = ProductHelper.getAllProductRecords(resultSet);
            productTable.setItems(products);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        p_name.clear();
        barcode_field.clear();
        price_field.clear();
        quantity_field.clear();
        supplierId_field.clear();
    }

    @FXML
    void onSave(ActionEvent event) throws IOException {
        String name = p_name.getText().trim();
        String barcode = barcode_field.getText().trim();
        String price = price_field.getText().trim();
        String quantity = quantity_field.getText().trim();
        String supplierId = supplierId_field.getText().trim();

        String selectQuery = "SELECT * from product WHERE barcode = ?";

        try {
            // check for duplicate barcode
            boolean checkDuplicate = GenericSQLHelper.checkDuplicate(selectQuery, barcode);

            if(name.isBlank() || barcode.isBlank() || supplierId.isBlank() || price.isBlank() || quantity.isBlank()){
                dialogBoxUtility.showDialogBox(7);
            } else if (checkDuplicate) {
                int result = actions.addNewProduct(name, barcode, price, quantity, supplierId);
                if (result == 1) {
                    dialogBoxUtility.showDialogBox(1);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }
            } else {
                dialogBoxUtility.showDialogBox(8);
            }

            resultSet = ProductHelper.getAllRecords();
            populateTableData(resultSet);
        } catch (Exception e) {
            dialogBoxUtility.showDialogBox(7);
        }
        clearFields();
    }


    @FXML
    void onSearch(ActionEvent event) {
        String searchInput = userInput.getText();

        try{
            resultSet = actions.searchProduct(searchInput);

            if(resultSet.next()) {
                p_name.setText(resultSet.getString("product_name"));
                barcode_field.setText(resultSet.getString("barcode"));
                price_field.setText(resultSet.getString("price"));
                quantity_field.setText(resultSet.getString("quantity"));
                supplierId_field.setText(resultSet.getString("supplier_id"));
            } else if(!resultSet.isBeforeFirst()) {
                dialogBoxUtility.showDialogBox(3);
            }

            ResultSet temp = ProductHelper.getSearchedList(searchInput);
            populateTableData(temp);

        } catch (Exception e){
            e.printStackTrace();
        }

        clearFields();
    }

    @FXML
    void onUpdate(ActionEvent event) throws SQLException, ClassNotFoundException {
        String name = p_name.getText().trim();
        String barcode = barcode_field.getText().trim();
        String price = price_field.getText().trim();
        String quantity = quantity_field.getText().trim();
        String supplierId = supplierId_field.getText().trim();

        //System.out.println("Values: " + name + " " + barcode + " " + price + " " + quantity + " " + supplierId);
        List<String> params = List.of(name, price, quantity, supplierId);
        String query = "SELECT * FROM product WHERE barcode = ?";

        try{
            boolean updateRequired = GenericSQLHelper.shouldUpdateRow(query, barcode, params);
            int result = 0;
            if(name.isBlank() || barcode.isBlank() || supplierId.isBlank()){
                dialogBoxUtility.showDialogBox(7);
            } else if(updateRequired){
                result = actions.updateProduct(barcode, name, price, quantity, supplierId);

                if (result == 2) {
                    dialogBoxUtility.showDialogBox(2);
                    resultSet = ProductHelper.getAllRecords();
                    populateTableData(resultSet);
                } else if (result == 0) {
                    dialogBoxUtility.showDialogBox(0);
                } else {
                    dialogBoxUtility.showDialogBox(7);
                }
            } else {
                dialogBoxUtility.showDialogBox(8);
            }
        } catch(SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        clearFields();
    }

    @FXML
    void onDelete(ActionEvent event) throws Exception{
        String deleteItem = barcode_field.getText();
        try{
            int result = actions.deleteProduct(deleteItem);
            if(result == 4){
                dialogBoxUtility.showDialogBox(4);
            } else {
                dialogBoxUtility.showDialogBox(5);
            }

            resultSet = ProductHelper.getAllRecords();
            populateTableData(resultSet);

        } catch(Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        clearFields();
    }

    @FXML
    void generateBarcode(ActionEvent event) {
        String additionalDetails = p_name.getText();
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/pos/inventorysystem/barcode.fxml"));
            Parent childRoot = fxmlLoader.load();
            Scene childScene = new Scene(childRoot);

            if (!additionalDetails.isEmpty() || !additionalDetails.isBlank()) {
                BarcodeController barcodeController = fxmlLoader.getController();
                barcodeController.generateBarcode(additionalDetails);

                Stage childStage = new Stage();
                childStage.setTitle("New Barcode");
                childStage.setScene(childScene);
                childStage.show();
            } else {
                DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();
                dialogBoxUtility.showDialogBox(7);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
