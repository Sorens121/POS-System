package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Product;
import com.pos.inventorysystem.actions.ProductActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductHelper {
    public static ObservableList<Product> getAllRecords() throws SQLException, ClassNotFoundException{
        ProductActions actions = new ProductActions();
        try{
            ResultSet resultSet = actions.getAllProducts();
            ObservableList<Product> list = getAllProductList(resultSet);
            return list;
        } catch (SQLException e) {
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }

    public static ObservableList<Product> getSearchedList(String input) throws SQLException, ClassNotFoundException {
        ProductActions actions = new ProductActions();
        try{
            ResultSet resultSet = actions.searchProduct(input);
            ObservableList<Product> list = getAllProductList(resultSet);
            return list;
        } catch (SQLException e){
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }

//    public static ObservableList<Product> getSearchedListByDetails(String name, String barcode) throws SQLException, ClassNotFoundException {
//        ProductActions actions = new ProductActions();
//        try {
//            ResultSet resultSet = actions.searchProductByDetails(name, barcode);
//            ObservableList<Product> list = getAllProductList(resultSet);
//            return list;
//        } catch (SQLException e) {
//            System.out.println("Error showing table");
//            e.printStackTrace();
//            throw e;
//        }
//    }

    private static ObservableList<Product> getAllProductList(ResultSet resultSet) throws ClassNotFoundException, SQLException{
        try{
            ObservableList<Product> productsList = FXCollections.observableArrayList();
            while(resultSet.next()) {
                Product product = new Product();
                //product.setProductId(resultSet.getString("pid"));
                product.setProductName(resultSet.getString("product_name"));
                product.setBarcode(resultSet.getString("barcode"));
                product.setPrice(resultSet.getInt("price"));
                product.setQuantity(resultSet.getInt("quantity"));
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
