package com.pos.inventorysystem.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class InvoiceController {

    @FXML
    private TableView<?> invoiceTable;

    @FXML
    private TableColumn<?, ?> barcode;

    @FXML
    private TableColumn<?, ?> invoiceId;

    @FXML
    private TableColumn<?, ?> quantity;

    @FXML
    private TableColumn<?, ?> totalPrice;

    @FXML
    private TableColumn<?, ?> unitPrice;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private Label onDateLabel;

    @FXML
    private Label invoiceNo;

    @FXML
    private TextField balanceDueField;

    @FXML
    private TextField customer_name;

    @FXML
    private TextField customer_no;

    @FXML
    private TextField paidAmtField;

    @FXML
    private TextField quantity_field;

    @FXML
    private TextField totalAmtField;

    @FXML
    private TextField search_customer_field;

    @FXML
    private ComboBox<?> productDrop;


    @FXML
    private ImageView search_product_btn;



    @FXML
    private Label unitPriceLabel;

    @FXML
    void initialize() {
        customer_no.setDisable(true);
        customer_name.setDisable(true);
        //search customer
        search_customer_field.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    String customerSearchInput = search_customer_field.getText();
                    System.out.println("Enter button pressed and the value is : " + customerSearchInput);
                }
            }
        });


        //search product
        search_product_btn.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("Search Product Image clicked");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pos/inventorysystem/popupTable.fxml"));
            Parent popupTable = null;
            try {
                popupTable = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("Error loading table layout");
            }

            Stage popUpTableStage = new Stage();
            popUpTableStage.setTitle("Search Product");
            popUpTableStage.setScene(new Scene(popupTable));
            popUpTableStage.show();

        });
    }

    @FXML
    void onAddToCart(ActionEvent event) {

    }

    @FXML
    void onPayAndPrint(ActionEvent event) {

    }

    @FXML
    void onProdDrop(ActionEvent event) {

    }

    @FXML
    void onRemove(ActionEvent event) {

    }

    @FXML
    void onRemoveAll(ActionEvent event) {

    }
    // time 5:31:30

}
