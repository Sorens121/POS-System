package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.Model.Invoice;
import com.pos.inventorysystem.Model.Items;
import com.pos.inventorysystem.db.db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Map;

public class InvoiceActions {
    private Statement statement;
    private ResultSet resultSet = null;
    private ObservableList<Invoice> data = null;

    public ObservableList<Invoice> getAllRecords() throws SQLException {
        try {
            return getAllInvoices();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer createNewRecord(Map<String, Object> parameter, ObservableList<Items> itemData) throws SQLException {
        try {
            return createNewInvoice(parameter, itemData);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private ObservableList<Invoice> getAllInvoices() throws SQLException {
        Connection connection = db.establishConnection();
        String query = "SELECT * FROM invoices";

        if(connection != null) {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                data = convertData(resultSet);
            } catch (SQLException e) {
                System.out.println("Error running the query");
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, statement, null, resultSet);
            }
        }

        return data;
    }

    private Integer createNewInvoice(Map<String, Object> parameter, ObservableList<Items> itemData) throws SQLException {
        Connection connection = db.establishConnection();
        /*
        --> Invoice Model
        * String invoice_id,
        * String customer_id,
        * String invoice_date,
        * double total_amount
        --> Items Model
        * String invoice_id
        * String barcode
        * String item_name
        * int quantity
        * double unit_price
        * double total_price
        */
        String invoice_id = (String) parameter.get("invoice_id");
        String customer_id = (String) parameter.get("customer_id");
        String invoice_date = (String) parameter.get("invoice_date");
        double total_amount = (Double) parameter.get("total_amount");
        String bucket_id = (String) parameter.get("bucket_id");

        //System.out.println("Query data Invoice id: " + invoice_id + ", Customer Id: " + customer_id + ", Date: " + invoice_date + ", Total Amount: " + total_amount);

        int output, result = -1;

        if(connection != null) {
            String invoiceQuery = "INSERT INTO invoices (invoice_id, customer_id, invoice_date, total_amount, bucket_id) VALUES (?, ?, ?, ?, ?)";
            String insertItemQuery = "INSERT INTO items (invoice_id, item_id, item_name, quantity, unit_price, total_price, bucket_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement invoiceStatement = connection.prepareStatement(invoiceQuery);
            PreparedStatement itemStatement = connection.prepareStatement(insertItemQuery);
            try {
                // Insert the invoice data
                invoiceStatement.setString(1, invoice_id);
                invoiceStatement.setString(2, customer_id);
                invoiceStatement.setString(3, invoice_date);
                invoiceStatement.setDouble(4, total_amount);
                invoiceStatement.setString(5, bucket_id);

                output = invoiceStatement.executeUpdate();

                if (output != 0 && output != -1) {
                    // Insert the invoice items

                    for (Items item : itemData) {
                        itemStatement.setString(1, invoice_id);
                        itemStatement.setString(2, item.getItemId());
                        itemStatement.setString(3, item.getItemName());
                        itemStatement.setInt(4, item.getQuantity());
                        itemStatement.setDouble(5, item.getUnitPrice());
                        itemStatement.setDouble(6, item.getTotalPrice());
                        itemStatement.setString(7, bucket_id);

                        result = itemStatement.executeUpdate();
                    }
                }

                invoiceStatement.close();
                return result;
            } catch (SQLException e) {
                System.out.println("Error running query");
                e.printStackTrace();
                throw e;
            } finally {
                db.closeConnection(connection, null, itemStatement, null);
            }
        }

        return result;
    }


    private ObservableList<Invoice> convertData(ResultSet resultSet) throws SQLException {
        ObservableList<Invoice> invoiceList = FXCollections.observableArrayList();

        try {
            while(resultSet.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(resultSet.getString("invoice_id"));
                invoice.setCustomerId(resultSet.getString("customer_id"));
                invoice.setBucketId(resultSet.getString("bucket_id"));
                invoice.setDate(resultSet.getString("invoice_date"));
                invoice.setTotalAmount(resultSet.getDouble("total_amount"));

                invoiceList.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return invoiceList;
    }

}
