package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Product;
import com.pos.inventorysystem.actions.ProductActions;
import com.pos.inventorysystem.helpers.ProductHelper;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private TableColumn<Product, String> pid;

    @FXML
    private TableView<Product> product_table;

    @FXML
    private TableColumn<Product, String> supplier_id;

    @FXML
    private TableColumn<Product, String> product_name;

    @FXML
    private TableColumn<Product, String> barcode;

    @FXML
    private TableColumn<Product, Integer> quantity;

    @FXML
    private TableColumn<Product, Integer> price;

    @FXML
    private BarcodeController barcodeController;

    private ObservableList<Product> products = null;
    private ResultSet resultSet = null;

    //private static final String CREATE_TABLE_QUERY = "CREATE TABLE product (pid INT AUTO_INCREMENT, product_name VARCHAR(45), barcode VARCHAR(12) NOT NULL, price INT DEFAULT 0, quantity INT DEFAULT 0, supplier_id VARCHAR(10) DEFAULT NULL, PRIMARY KEY(barcode), UNIQUE (pid, barcode))";
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE product (product_name VARCHAR(45), barcode VARCHAR(12) NOT NULL, price INT DEFAULT 0, quantity INT DEFAULT 0, supplier_id VARCHAR(10) DEFAULT NULL, PRIMARY KEY(barcode), UNIQUE (barcode))";

    DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();
    ProductActions actions = new ProductActions();

    @FXML
    private void initialize() throws ClassNotFoundException, SQLException {
        try{
            //check if table exist
            boolean tableExist = TableUtility.checkTableExists("product");

            if(!tableExist) {
                TableUtility.createTable(CREATE_TABLE_QUERY);
                System.out.println("Table created successfully");
            } else {
                System.out.println("Table already exist");
            }
        } catch(SQLException | ClassNotFoundException e) {
            System.out.println("Database error occurred: " + e.getMessage());
        }

        //setting up products table
        pid.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(product_table.getItems().indexOf(column.getValue()) + 1).asString());
        pid.setSortable(false);
        product_name.setCellValueFactory(cellData -> cellData.getValue().getProductNameProperty());
        barcode.setCellValueFactory(cellData -> cellData.getValue().getBarcodeProperty());
        price.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty().asObject());
        quantity.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty().asObject());
        supplier_id.setCellValueFactory(cellData -> cellData.getValue().getSupplierIdProperty());

        products = ProductHelper.getAllRecords();
        populateTable(products);

        System.out.print("Table data: " + product_name + barcode + price + quantity + supplier_id);

        //display details in their respective fields when items are selected from the table
        product_table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                p_name.setText(newValue.getProductName());
                barcode_field.setText(newValue.getBarcode());
                price_field.setText(String.valueOf(newValue.getPrice()));
                quantity_field.setText(String.valueOf(newValue.getQuantity()));
                supplierId_field.setText(newValue.getSupplierId());
            }

            System.out.println("On table row selection: " + p_name + barcode_field + price_field + quantity_field + supplierId_field);
        });
    }

    private void populateTable(ObservableList<Product> products) {
        product_table.setItems(products);
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
        String name = p_name.getText();
        String barcode = barcode_field.getText();
        int price = Integer.parseInt(price_field.getText());
        int quantity = Integer.parseInt(quantity_field.getText());
        String supplierId = supplierId_field.getText();

        try {
            if(!name.isEmpty() && !barcode.isEmpty()) {
                int result = actions.addNewProduct(name, barcode, price, quantity, supplierId);
                if(result == 1) {
                    dialogBoxUtility.showDialogBox(1);
                } else {
                    dialogBoxUtility.showDialogBox(0);
                }
                products = ProductHelper.getAllRecords();
                populateTable(products);
            }
        } catch (Exception e) {
            dialogBoxUtility.showDialogBox(7);
        }
        clearFields();
    }

    @FXML
    void onSearch(ActionEvent event) {

    }

    @FXML
    void onSearchInfo(ActionEvent event) {
        String searchInput = userInput.getText();

        try{
            resultSet = actions.searchProduct(searchInput);
            if(resultSet.next()) {
                p_name.setText(resultSet.getString("product_name"));
                barcode_field.setText(resultSet.getString("barcode"));
                price_field.setText(String.valueOf(resultSet.getInt("price")));
                quantity_field.setText(String.valueOf(resultSet.getInt("quantity")));
                supplierId_field.setText(resultSet.getString("supplier_id"));
            } else if(!resultSet.isBeforeFirst()) {
                dialogBoxUtility.showDialogBox(3);
            }

            products = ProductHelper.getSearchedList(searchInput);
            populateTable(products);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void onUpdate(ActionEvent event) throws SQLException, ClassNotFoundException {
        String name = p_name.getText();
        String barcode = barcode_field.getText();
        int price = Integer.parseInt(price_field.getText());
        int quantity = Integer.parseInt(quantity_field.getText());
        String supplierId = supplierId_field.getText();

        System.out.println("Values: " + name + " " + barcode + " " + price + " " + quantity + " " + supplierId);

        try{
            int result = 0;
            result = actions.updateProduct(barcode, name, price, quantity, supplierId);

            if (result == 2) {
                dialogBoxUtility.showDialogBox(2);
                products = ProductHelper.getAllRecords();
                populateTable(products);
            } else if (result == 0) {
                dialogBoxUtility.showDialogBox(0);
            } else {
                dialogBoxUtility.showDialogBox(7);
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

            products = ProductHelper.getAllRecords();
            populateTable(products);

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
