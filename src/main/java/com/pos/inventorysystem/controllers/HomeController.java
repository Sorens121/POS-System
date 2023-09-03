package com.pos.inventorysystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController implements Initializable {

    @FXML
    private Button customerBtn;

    @FXML
    private Button employeeBtn;

    @FXML
    private Button invoiceBtn;

    @FXML
    private Button productBtn;

    @FXML
    private Button reportsBtn;

    @FXML
    private Button salesBtn;

    @FXML
    private Button supplierBtn;

    @FXML
    private StackPane contentArea;

    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/pos/inventorysystem/customer.fxml"));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(root);
        } catch (IOException e) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    void OnCustomerClick(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("/com/pos/inventorysystem/customer.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void OnEmployeeClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/pos/inventorysystem/employes.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void OnInvoiceClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/pos/inventorysystem/invoice.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void OnProductClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/pos/inventorysystem/Product.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void OnReportsClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/pos/inventorysystem/reports.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void OnSalesClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/pos/inventorysystem/sales.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void OnSupplierClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/pos/inventorysystem/supplier.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }


}
