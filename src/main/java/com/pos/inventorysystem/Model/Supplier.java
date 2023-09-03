package com.pos.inventorysystem.Model;

import javafx.beans.property.SimpleStringProperty;

public class Supplier {
    private SimpleStringProperty supplierId;
    private SimpleStringProperty supplierName;
    private SimpleStringProperty supplierTpNo;

    public Supplier() {
        this.supplierId = new SimpleStringProperty();
        this.supplierName = new SimpleStringProperty();
        this.supplierTpNo = new SimpleStringProperty();
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

    public String getSupplierTpNo() {
        return supplierTpNo.get();
    }

    public SimpleStringProperty getSupplierTpNoProperty() {
        return supplierTpNo;
    }

    public void setSupplierTpNo(String supplierTpNo) {
        this.supplierTpNo.set(supplierTpNo);
    }
}
