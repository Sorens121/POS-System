package com.pos.inventorysystem.Model;

import javafx.beans.property.SimpleStringProperty;

public class Supplier {
    private final SimpleStringProperty supplierId;
    private final SimpleStringProperty supplierName;
    private final SimpleStringProperty supplierContactNo;
    private final SimpleStringProperty supplierEmail;
    private final SimpleStringProperty companyName;

    public Supplier() {
        this.supplierId = new SimpleStringProperty();
        this.supplierName = new SimpleStringProperty();
        this.supplierContactNo = new SimpleStringProperty();
        this.supplierEmail = new SimpleStringProperty();
        this.companyName = new SimpleStringProperty();
    }

    public String getSupplierId() {
        return supplierId.get();
    }

    public SimpleStringProperty getSupplierIdProperty() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId.set(supplierId);
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public SimpleStringProperty getSupplierNameProperty() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.set(supplierName);
    }

    public String getSupplierContactNo() {
        return supplierContactNo.get();
    }

    public SimpleStringProperty getSupplierContactNoProperty() {
        return supplierContactNo;
    }

    public void setSupplierContactNo(String supplierContactNo) {
        this.supplierContactNo.set(supplierContactNo);
    }

    public String getSupplierEmail() {
        return supplierEmail.get();
    }

    public SimpleStringProperty getSupplierEmailProperty() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail.set(supplierEmail);
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public SimpleStringProperty getCompanyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }
}
