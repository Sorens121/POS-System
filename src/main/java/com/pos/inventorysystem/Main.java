package com.pos.inventorysystem;

import com.pos.inventorysystem.controllers.InvoiceController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    //private static Stage primaryStage;
    public static boolean configFileExists() {
        String configFilePath = "C:/Users/TROJAN HORSE/Java Project/POS Inventory System/src/main/resources/config.properties";;
        File configFile = new File(configFilePath);

        return configFile.exists();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //check if the config file exists
        if(!configFileExists()){
            Platform.runLater(() -> showErrorDialog("Configuration file not found.", "Please make sure that config file is present."));
            return;
        }

        // Initializing and starting the javafx application here
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("POS System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // TODO: 05-09-2023  Need to create a splash screen while the application searches for config.properties file
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
