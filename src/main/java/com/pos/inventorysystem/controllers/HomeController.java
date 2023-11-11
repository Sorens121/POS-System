package com.pos.inventorysystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController implements Initializable {

    @FXML
    private Pane contentArea;

    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/pos/inventorysystem/customer.fxml")));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(root);
        } catch (IOException e) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    void OnCustomerClick(ActionEvent event) throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/pos/inventorysystem/customer.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void OnEmployeeClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/pos/inventorysystem/employes.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void OnInvoiceClick(ActionEvent event) throws IOException {
        FXMLLoader loader =  new FXMLLoader(getClass().getResource("/com/pos/inventorysystem/invoice.fxml"));
        root = loader.load();
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);

        // method to get the stage to implement modality feature
        Node node = root.getScene().getRoot();
        Stage mainStage = (Stage) node.getScene().getWindow();
        InvoiceController invoiceController = loader.getController();
        invoiceController.setMainStage(mainStage);

    }

    @FXML
    void OnProductClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/pos/inventorysystem/Product.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void OnReportsClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/pos/inventorysystem/reports.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void OnSalesClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/pos/inventorysystem/sales.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void OnSupplierClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/pos/inventorysystem/supplier.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }


}
