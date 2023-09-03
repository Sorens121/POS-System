package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.utils.BarcodeGenerator;
import com.pos.inventorysystem.utils.GenericUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class BarcodeController {
    @FXML
    private ImageView barcodeImg;

    @FXML
    private Label barcodeLabel;

    @FXML
    private Button clipboardBtn;

    @FXML
    private void initialize() {
        clipboardBtn.setOnAction(e -> {
            String textToCopy = barcodeLabel.getText();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(textToCopy);
            clipboard.setContent(clipboardContent);

            showConfirmation();
        });
    }

    @FXML
    void onClipboardClick(ActionEvent event) {

    }

    public void generateBarcode(String additionalDetails) {
        BarcodeGenerator barcodeGenerator = new BarcodeGenerator();
        String content = GenericUtils.GenerateBarcode();

        Canvas canvas = barcodeGenerator.generateBarcodeImageWithAdditionalText(content, additionalDetails);
        barcodeImg.setImage(canvas.snapshot(null, null));
        barcodeLabel.setText(content);
    }

    private void showConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Copied");
        alert.showAndWait();
    }


}
