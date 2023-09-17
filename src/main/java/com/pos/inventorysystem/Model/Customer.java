package com.pos.inventorysystem.Model;

import com.mysql.cj.conf.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {
    private SimpleStringProperty customerName;
    private SimpleStringProperty customerId;
    private SimpleStringProperty contactNumber;
    private SimpleStringProperty email;
    private SimpleStringProperty[] values;

    public Customer() {
        this.customerId = new SimpleStringProperty();
        this.customerName = new SimpleStringProperty();
        this.contactNumber = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.values = new SimpleStringProperty[]{new SimpleStringProperty()};
    }

    public Customer(String... values) {
        this.values = new SimpleStringProperty[values.length];
        for(int i = 0; i < values.length; i++) {
            this.values[i] = new SimpleStringProperty(values[i]);
        }
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

    public SimpleStringProperty getValueAt(int index) {
        if(index >= 0 && index < values.length){
            return values[index];
        }
        return new SimpleStringProperty("");
    }
}
