package com.pos.inventorysystem.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
    private SimpleStringProperty productId;
    private SimpleStringProperty productName;
    private SimpleStringProperty barcode;
    private SimpleIntegerProperty price;
    private SimpleIntegerProperty quantity;
    private SimpleStringProperty supplierId;

    public Product() {
        this.productId = new SimpleStringProperty();
        this.productName = new SimpleStringProperty();
        this.barcode = new SimpleStringProperty();
        this.price = new SimpleIntegerProperty();
        this.quantity = new SimpleIntegerProperty();
        this.supplierId = new SimpleStringProperty();
    }
    public String getProductId() {
        return productId.get();
    }

    public SimpleStringProperty getProductIdProperty() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId.set(productId);
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

    public int getPrice() {
        return price.get();
    }

    public SimpleIntegerProperty getPriceProperty() {
        return price;
    }

    public void setPrice(int price) {
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
