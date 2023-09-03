package com.pos.inventorysystem.Model;

import javafx.beans.property.SimpleStringProperty;

public class Customer {
    private SimpleStringProperty customerId;
    private SimpleStringProperty customerName;
    private SimpleStringProperty tpNumber;

    public Customer() {
        this.customerId = new SimpleStringProperty();
        this.customerName = new SimpleStringProperty();
        this.tpNumber = new SimpleStringProperty();
    }

    public String getCustomerId() {
        return customerId.get();
    }

    public SimpleStringProperty getCustomerIdProperty() {
        return customerId;
    }

    public void setCustomerId(String id) {
        this.customerId.set(id);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public SimpleStringProperty getCustomerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String name) {
        this.customerName.set(name);
    }

    public String getTpNumber() {
        return tpNumber.get();
    }

    public SimpleStringProperty getTpNumberProperty() {
        return tpNumber;
    }

    public void setTpNumber(String tp) {
        this.tpNumber.set(tp);
    }
}
