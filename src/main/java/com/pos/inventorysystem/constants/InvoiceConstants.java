package com.pos.inventorysystem.constants;

public class InvoiceConstants {
    private InvoiceConstants() {}

    public static final String CREATE_INVOICE_TABLE = "CREATE TABLE invoices (invoice_id VARCHAR(20) NOT NULL, customer_id VARCHAR(12), bucket_id VARCHAR(12), invoice_date DATE, total_amount DECIMAL(10, 2), PRIMARY KEY (invoice_id), FOREIGN KEY (customer_id) REFERENCES customer(customer_id), FOREIGN KEY (bucket_id) REFERENCES items(bucket_id))";
    public static final String CREATE_INVOICE_ITEMS_TABLE = "CREATE TABLE items (serial_no INT AUTO_INCREMENT, item_id VARCHAR(12), invoice_id VARCHAR(20), item_name VARCHAR(45), bucket_id VARCHAR(12), quantity INT, unit_price DECIMAL(10, 2), total_price DECIMAL(10, 2), PRIMARY KEY (serial_no), FOREIGN KEY(invoice_id) REFERENCES invoices(invoice_id))";
}
