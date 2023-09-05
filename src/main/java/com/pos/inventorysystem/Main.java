package com.pos.inventorysystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class Main extends Application {

    public static boolean configFileExists() {
        String configFilePath = "C:/Users/TROJAN HORSE/Java Project/POS Inventory System/src/main/resources/config.properties";;
        File configFile = new File(configFilePath);

        return configFile.exists();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //check if the config file exists
        if(!configFileExists()){
            Platform.runLater(() -> showErrorDialog("Configuration file not found.", "Please make sure that config file is present."));
            return;
        }

        // Initializing and starting the javafx application here
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("POS System");
        stage.setScene(scene);
        stage.show();
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
