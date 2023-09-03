package com.pos.inventorysystem.Model;

import javafx.beans.property.SimpleStringProperty;

public class Employee {
    private SimpleStringProperty employeeId;
    private SimpleStringProperty employeeName;
    private SimpleStringProperty employeeTpNumber;

    public Employee() {
        this.employeeId = new SimpleStringProperty();
        this.employeeName = new SimpleStringProperty();
        this.employeeTpNumber = new SimpleStringProperty();
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

    public String getEmployeeTpNumber() {
        return employeeTpNumber.get();
    }

    public SimpleStringProperty getEmployeeTpNumberProperty() {
        return employeeTpNumber;
    }

    public void setEmployeeTpNumber(String employeeTpNumber) {
        this.employeeTpNumber.set(employeeTpNumber);
    }
}
