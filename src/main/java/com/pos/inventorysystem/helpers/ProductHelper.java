package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Product;
import com.pos.inventorysystem.actions.ProductActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductHelper {
    public static ObservableList<Product> getAllProductRecords(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        try {
            return getAllProductList(resultSet);
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static ResultSet getAllRecords() throws SQLException, ClassNotFoundException{
        ProductActions actions = new ProductActions();
        try{
            ResultSet resultSet = actions.getAllProducts();
            //ObservableList<Product> list = getAllProductList(resultSet);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }

    public static ResultSet getSearchedList(String input) throws SQLException, ClassNotFoundException {
        ProductActions actions = new ProductActions();
        try{
            ResultSet resultSet = actions.searchProduct(input);
            //ObservableList<Product> list = getAllProductList(resultSet);
            return resultSet;
        } catch (SQLException e){
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }


    private static ObservableList<Product> getAllProductList(ResultSet resultSet) throws ClassNotFoundException, SQLException{
        try{
            ObservableList<Product> productsList = FXCollections.observableArrayList();
            while(resultSet.next()) {
                Product product = new Product();
                product.setProductName(resultSet.getString("product_name"));
                product.setBarcode(resultSet.getString("barcode"));
                product.setPrice(resultSet.getString("price"));
                product.setQuantity(resultSet.getString("quantity"));
                product.setSupplierId(resultSet.getString("supplier_id"));

                productsList.add(product);
            }

            return productsList;
        } catch(SQLException e) {
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }
}
