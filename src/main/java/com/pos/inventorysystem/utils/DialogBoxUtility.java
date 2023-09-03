package com.pos.inventorysystem.utils;

import com.pos.inventorysystem.controllers.DialogBoxController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DialogBoxUtility {
    public void showDialogBox(int state) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/pos/inventorysystem/dialogBox.fxml"));
        Parent parent = null;

        try{
            parent = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DialogBoxController dialogBoxController = fxmlLoader.<DialogBoxController>getController();
        dialogBoxController.getMessageFromMsgHelper(state);

        //display the layout
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
