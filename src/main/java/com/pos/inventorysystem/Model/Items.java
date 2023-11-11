package com.pos.inventorysystem.Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Items {
    private final SimpleStringProperty invoice_id;
    private final SimpleStringProperty item_id;
    private final SimpleStringProperty item_name;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty unit_price;
    private final SimpleDoubleProperty total_price;

    public Items() {
        this.invoice_id = new SimpleStringProperty();
        this.item_id = new SimpleStringProperty();
        this.item_name = new SimpleStringProperty();
        this.quantity = new SimpleIntegerProperty();
        this.unit_price = new SimpleDoubleProperty();
        this.total_price = new SimpleDoubleProperty();
    }

    public String getInvoiceId() {
        return invoice_id.get();
    }

    public SimpleStringProperty getInvoiceIdProperty() {
        return invoice_id;
    }

    public void setInvoiceId(String invoice_id) {
        this.invoice_id.set(invoice_id);
    }

    public String getItemId() {
        return item_id.get();
    }

    public SimpleStringProperty getItemIdProperty() {
        return item_id;
    }

    public void setItemId(String item_id) {
        this.item_id.set(item_id);
    }

    public String getItemName() {
        return item_name.get();
    }

    public SimpleStringProperty getItemNameProperty() {
        return item_name;
    }

    public void setItemName(String item_name) {
        this.item_name.set(item_name);
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

    public double getUnitPrice() {
        return unit_price.get();
    }

    public SimpleDoubleProperty getUnitPriceProperty() {
        return unit_price;
    }

    public void setUnitPrice(double unit_price) {
        this.unit_price.set(unit_price);
    }

    public double getTotalPrice() {
        return total_price.get();
    }

    public SimpleDoubleProperty getTotalPriceProperty() {
        return total_price;
    }

    public void setTotalPrice(double total_price) {
        this.total_price.set(total_price);
    }
}
