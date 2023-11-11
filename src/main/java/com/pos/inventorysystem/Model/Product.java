package com.pos.inventorysystem.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
    private final SimpleStringProperty productName;
    private final SimpleStringProperty barcode;
    private final SimpleStringProperty price;
    private final SimpleIntegerProperty quantity;
    private final SimpleStringProperty supplierId;

    public Product() {
        this.productName = new SimpleStringProperty();
        this.barcode = new SimpleStringProperty();
        this.price = new SimpleStringProperty();
        this.quantity = new SimpleIntegerProperty();
        this.supplierId = new SimpleStringProperty();
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty getProductNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getBarcode() {
        return barcode.get();
    }

    public SimpleStringProperty getBarcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty getPriceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty getQuantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public String getSupplierId() {
        return supplierId.get();
    }

    public SimpleStringProperty getSupplierIdProperty() {
        return supplierId;
    }

    public void setSupplierId(String suppliedId) {
        this.supplierId.set(suppliedId);
    }
}
