package com.pos.inventorysystem.Model;

import javafx.beans.property.SimpleStringProperty;

public class Employee {
    private SimpleStringProperty employeeId;
    private SimpleStringProperty employeeName;
    private SimpleStringProperty contactNo;
    private SimpleStringProperty email;

    public Employee() {
        this.employeeId = new SimpleStringProperty();
        this.employeeName = new SimpleStringProperty();
        this.contactNo = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    public String getEmployeeId() {
        return employeeId.get();
    }

    public SimpleStringProperty getEmployeeIdProperty() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId.set(employeeId);
    }

    public String getEmployeeName() {
        return employeeName.get();
    }

    public SimpleStringProperty getEmployeeNameProperty() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName.set(employeeName);
    }

    public String getContactNo() {
        return contactNo.get();
    }

    public SimpleStringProperty getContactNoProperty() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo.set(contactNo);
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
