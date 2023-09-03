module com.pos.posinventorysystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.logging;
    requires java.sql;
    requires mysql.connector.j;
    requires com.google.zxing;
    requires java.desktop;


    exports com.pos.inventorysystem;
    opens com.pos.inventorysystem to javafx.fxml;
    exports com.pos.inventorysystem.Model;
    opens com.pos.inventorysystem.Model to javafx.fxml;
    exports com.pos.inventorysystem.demo.layouts;
    opens com.pos.inventorysystem.demo.layouts to javafx.fxml;
    exports com.pos.inventorysystem.controllers;
    opens com.pos.inventorysystem.controllers to javafx.fxml;

}