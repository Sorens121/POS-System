package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.Model.Product;
import com.pos.inventorysystem.db.db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PopUpTableActions {
    private ObservableList<Product> data = null;
    private ResultSet resultSet = null;

    public ObservableList<Product> getAllRecords(String query) throws SQLException {
        try {
            return searchProduct(query);
        } catch (SQLException e) {
            System.out.println("Error fetching data");
            throw e;
        }
    }

    private ObservableList<Product> searchProduct(String searchInput) throws SQLException {
        Connection connection = db.establishConnection();
        String searchQuery = "SELECT * FROM product where product_name LIKE ? OR barcode LIKE ?";
        String wildCard = "%" + searchInput + "%";

        if(connection != null) {
            PreparedStatement ps = connection.prepareStatement(searchQuery);
            try {
                ps.setString(1, wildCard);
                ps.setString(2, searchInput);

                resultSet = ps.executeQuery();
                data = convertData(resultSet);
            } catch (SQLException e) {
                System.out.println("Error while running the query: " + e.getMessage());
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, null, ps, resultSet);
            }
        }

        return data;
    }

    private static ObservableList<Product> convertData(ResultSet resultSet) throws SQLException {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        try {
            while(resultSet.next()) {
                Product product = new Product();
                product.setBarcode(resultSet.getString("barcode"));
                product.setProductName(resultSet.getString("product_name"));
                product.setPrice(resultSet.getString("price"));
                product.setQuantity(Integer.parseInt(resultSet.getString("quantity")));
                product.setSupplierId(resultSet.getString("supplier_id"));

                productList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return productList;
    }
}
