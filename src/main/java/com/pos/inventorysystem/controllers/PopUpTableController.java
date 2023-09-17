package com.pos.inventorysystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopUpTableController {

    @FXML
    private TextField search_product_field;

    @FXML
    private Button searchBtn;

    @FXML
    private Button addNCloseBtn;

    @FXML
    private TableView<?> searchProductList;

    @FXML
    private TableColumn<?, ?> barcode;

    @FXML
    private TableColumn<?, ?> price;

    @FXML
    private TableColumn<?, ?> product_name;

    @FXML
    private TableColumn<?, ?> serial_no;

    @FXML
    void onAddProduct(ActionEvent event) {
        //Get the current stage (window)
        Stage stage = (Stage) addNCloseBtn.getScene().getWindow();

        //close the stage
        stage.close();
    }

}
