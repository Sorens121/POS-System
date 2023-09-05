package com.pos.inventorysystem.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {
    private SimpleStringProperty customerName;
    private SimpleStringProperty customerId;
    private SimpleStringProperty contactNumber;
    private SimpleStringProperty email;

    public Customer() {
        this.customerId = new SimpleStringProperty();
        this.customerName = new SimpleStringProperty();
        this.contactNumber = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
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

    public String getCustomerId() {
        return customerId.get();
    }

    public String getContactNumber() {
        return contactNumber.get();
    }

    public SimpleStringProperty getContactNumberProperty() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber.set(contactNumber);
    }

    public SimpleStringProperty getCustomerIdProperty() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId.set(customerId);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty getEmailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
