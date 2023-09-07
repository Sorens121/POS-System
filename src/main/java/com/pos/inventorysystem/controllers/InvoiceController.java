package com.pos.inventorysystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class InvoiceController {

    @FXML
    private TextField balanceDueField;

    @FXML
    private TextField paidAmtField;

    @FXML
    private TextField quantity_field;

    @FXML
    private TextField totalAmtField;

    @FXML
    private Label invoiceNo;

    @FXML
    private Label unitPriceLabel;

    @FXML
    private ComboBox<?> customerDrop;

    @FXML
    private ComboBox<?> productDrop;

    @FXML
    private TableView<?> invoiceTable;

    @FXML
    private TableColumn<?, ?> invoiceId;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TableColumn<?, ?> barcode;

    @FXML
    private TableColumn<?, ?> quantity;

    @FXML
    private TableColumn<?, ?> unitPrice;

    @FXML
    private TableColumn<?, ?> totalPrice;


    @FXML
    void onAddToCart(ActionEvent event) {

    }

    @FXML
    void onCustDrop(ActionEvent event) {

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
