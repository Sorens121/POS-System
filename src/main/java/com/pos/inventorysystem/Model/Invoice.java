package com.pos.inventorysystem.Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Invoice {
    private final SimpleStringProperty invoice_id;
    private final SimpleStringProperty customer_id;
    private final SimpleStringProperty date;
    private final SimpleStringProperty bucket_id;
    private final SimpleDoubleProperty total_amount;

    public Invoice() {
        this.invoice_id = new SimpleStringProperty();
        this.customer_id = new SimpleStringProperty();
        this.date = new SimpleStringProperty();
        this.bucket_id = new SimpleStringProperty();
        this.total_amount = new SimpleDoubleProperty();
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

    public String getCustomerId() {
        return customer_id.get();
    }

    public SimpleStringProperty getCustomerIdProperty() {
        return customer_id;
    }

    public void setCustomerId(String customer_id) {
        this.customer_id.set(customer_id);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty getDateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public double getTotalAmount() {
        return total_amount.get();
    }

    public SimpleDoubleProperty getTotalAmountProperty() {
        return total_amount;
    }

    public void setTotalAmount(double total_amount) {
        this.total_amount.set(total_amount);
    }

    public String getBucketId() {
        return bucket_id.get();
    }

    public SimpleStringProperty bucketIdProperty() {
        return bucket_id;
    }

    public void setBucketId(String bucket_id) {
        this.bucket_id.set(bucket_id);
    }
}
