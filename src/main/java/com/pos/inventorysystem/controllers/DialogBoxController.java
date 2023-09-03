package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.helpers.DialogMsgHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DialogBoxController {
    @FXML
    private AnchorPane dialogBoxMain;
    @FXML
    private Text disDialogMsg;

    @FXML
    private Button okBtn;

    @FXML
    private void initialize() {
        handleClickEvent();
    }

    private void handleClickEvent() {
        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeStage(event);
            }
        });
    }

    public void getMessageFromMsgHelper(int state) {
        DialogMsgHelper dialogMsgHelper = new DialogMsgHelper();
        disDialogMsg.setText(dialogMsgHelper.displayDialogMsg(state));

        // apply fx-css
        if(state == 0){
            dialogBoxMain.setStyle("-fx-background-color: #ff8080"); // Error msg
        } else if (state == 1) {
            dialogBoxMain.setStyle("-fx-background-color: #b3ff99"); // Successful save
        } else if (state == 2) {
            dialogBoxMain.setStyle("-fx-background-color: #ffff99"); // Successful update
        } else{
            dialogBoxMain.setStyle("-fx-background-color: #ffffff"); // default
        }
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
