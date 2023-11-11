package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.Model.Product;
import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.ErrorMsgHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ProductActions {
    private Statement statement;
    private ResultSet resultSet = null;
    private String query = null;
    private ObservableList<Product> data = null;

    public ObservableList<Product> getAllRecords() throws SQLException {
        try {
            return getAllProducts();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public ObservableList<Product> getOneRecord(String id) throws SQLException {
        try {
            return getOneProduct(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer createNewRecord(String name, String barcode, String price, String quantity, String supplierId) throws SQLException {
        try {
            return addNewProduct(name, barcode, price, quantity, supplierId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer updateRecord(String barcode, String name, String price, String quantity, String supplierId) throws SQLException {
        try {
            return updateProduct(barcode, name, price, quantity, supplierId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer deleteRecord(String id) throws SQLException {
        try {
            return deleteProduct(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<Product> searchRecord(String searchInput) throws SQLException {
        try {
            return searchProduct(searchInput);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private ObservableList<Product> getAllProducts() throws SQLException {
        Connection connection = db.establishConnection();
        query = "SELECT * FROM product";

        if(connection != null) {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                data = convertData(resultSet);
            } catch (SQLException e) {
                System.out.println("Error while running query");
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, statement, null, resultSet);
            }
        }
        return  data;
    }

    private ObservableList<Product> getOneProduct(String searchID) throws SQLException {
        Connection connection = db.establishConnection();
        query = "SELECT * FROM product WHERE barcode = '"+searchID+"'";

        if(connection != null) {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                data = convertData(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, statement, null, resultSet);
            }
        }
        return data;
    }

    private Integer addNewProduct(String name, String barcode, String price, String quantity, String supplierId) throws SQLException {
        Connection connection = db.establishConnection();
        query = "INSERT INTO product (product_name, barcode, price, quantity, supplier_id) VALUES ('"+name+"', '"+barcode+"', '"+price+"', '"+quantity+"', '"+supplierId+"')";
        int output = -1;

        if(connection != null) {
            try {
                statement = connection.createStatement();
                output = statement.executeUpdate(query);
                return output;
            } catch (SQLException e) {
                System.out.println("Error while running query");
                ErrorMsgHelper.catchError(String.valueOf(e.getErrorCode()));
            } finally {
                db.closeConnection(connection, statement, null, null);
            }
        }

        return output;
    }

    private Integer updateProduct(String barcode, String name, String price, String quantity, String supplierId) throws SQLException {
        Connection connection = db.establishConnection();
        String updateQuery = "UPDATE product SET ";
        int output = -1, index = 0;

        ObservableList<Product> oldValue = getOneProduct(barcode);

        String oldName = null, oldSupplierId = null, state = null, oldPrice = null, oldQuantity = null;

        while(index < oldValue.size()){
            oldName = oldValue.get(0).getProductName();
            oldPrice = oldValue.get(0).getPrice();
            oldQuantity = String.valueOf(oldValue.get(0).getQuantity());
            oldSupplierId = oldValue.get(0).getSupplierId();
            index++;
        }

        //Scenarios 2
        assert oldName != null;
        //NPQS
        if(!oldName.equalsIgnoreCase(name) && !oldPrice.equalsIgnoreCase(price) && !oldQuantity.equalsIgnoreCase(quantity) && !oldSupplierId.equalsIgnoreCase(supplierId)){
            updateQuery += "product_name = ?, price = ?, quantity = ?, supplier_id = ? WHERE barcode = ?";
            state = "1";
        }
        //NPQ
        else if(!oldName.equalsIgnoreCase(name) && !oldPrice.equalsIgnoreCase(price) && !oldQuantity.equalsIgnoreCase(quantity)){
            updateQuery += "product_name = ?, price = ?, quantity = ? WHERE barcode = ?";
            state = "2";
        }
        //NPS
        else if(!oldName.equalsIgnoreCase(name) && !oldPrice.equalsIgnoreCase(price) && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "product_name = ?, price = ?, supplier_id = ? WHERE barcode = ?";
            state = "3";
        }
        //NQS
        else if(!oldName.equalsIgnoreCase(name) && !oldQuantity.equalsIgnoreCase(quantity) && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "product_name = ?, quantity = ?, supplier_id = ? WHERE barcode = ?";
            state = "4";
        }
        //PQS
        else if(!oldPrice.equalsIgnoreCase(price) && !oldQuantity.equalsIgnoreCase(quantity) && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "price = ?, quantity = ?, supplier_id = ? WHERE barcode = ?";
            state = "5";
        }
        //NQ
        else if(!oldName.equalsIgnoreCase(name) && !oldQuantity.equalsIgnoreCase(quantity) ) {
            updateQuery += "product_name = ?, quantity = ? WHERE barcode = ?";
            state = "6";
        }
        //NP
        else if(!oldName.equalsIgnoreCase(name) && !oldPrice.equalsIgnoreCase(price)) {
            updateQuery += "product_name = ?, price = ? WHERE barcode = ?";
            state = "7";
        }
        //NS
        else if(!oldName.equalsIgnoreCase(name) && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "product_name = ?, supplier_id = ? WHERE barcode = ?";
            state = "8";
        }
        //PQ
        else if(!oldPrice.equalsIgnoreCase(price) && !oldQuantity.equalsIgnoreCase(quantity)) {
            updateQuery += "price = ?, quantity = ? WHERE barcode = ?";
            state = "9";
        }
        //PS
        else if(!oldPrice.equalsIgnoreCase(price) && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "price = ?, supplier_id = ? WHERE barcode = ?";
            state = "10";
        }
        //QS
        else if(!oldQuantity.equalsIgnoreCase(quantity) && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "quantity = ?, supplier_id = ? WHERE barcode = ?";
            state = "11";
        }
        //N
        else if(!oldName.equalsIgnoreCase(name)){
            updateQuery += "product_name = ? WHERE barcode = ?";
            state = "12";
        }
        //P
        else if(!oldPrice.equalsIgnoreCase(price)){
            updateQuery += "price = ? WHERE barcode = ?";
            state = "13";
        }
        //Q
        else if(!oldQuantity.equalsIgnoreCase(quantity)){
            updateQuery += "quantity = ? WHERE barcode = ?";
            state = "14";
        }
        //S
        else if(!oldSupplierId.equalsIgnoreCase(supplierId)){
            updateQuery += "supplier_id = ? WHERE barcode = ?";
            state = "15";
        }

        if(connection != null) {
            PreparedStatement ps = connection.prepareStatement(updateQuery);
            try {
                if (state != null) {
                    switch (state) {
                        case "1":
                            ps.setString(1, name);
                            ps.setString(2, price);
                            ps.setString(3, quantity);
                            ps.setString(4, supplierId);
                            ps.setString(5, barcode);
                            System.out.println("Scenario 1");
                            break;
                        case "2":
                            //NPQ
                            ps.setString(1, name);
                            ps.setString(2, price);
                            ps.setString(3, quantity);
                            ps.setString(4, barcode);
                            System.out.println("Scenario 2");
                            break;
                        case "3":
                            //NPS
                            ps.setString(1, name);
                            ps.setString(2, price);
                            ps.setString(3, supplierId);
                            ps.setString(4, barcode);
                            System.out.println("Scenario 3");
                            break;

                        case "4":
                            //NQS
                            ps.setString(1, name);
                            ps.setString(2, quantity);
                            ps.setString(3, supplierId);
                            ps.setString(4, barcode);
                            System.out.println("Scenario 4");
                            break;

                        case "5":
                            //PQS
                            ps.setString(1, price);
                            ps.setString(2, quantity);
                            ps.setString(3, supplierId);
                            ps.setString(4, barcode);
                            System.out.println("Scenario 5");
                            break;

                        case "6":
                            //NQ
                            ps.setString(1, name);
                            ps.setString(2, quantity);
                            ps.setString(3, barcode);
                            System.out.println("Scenario 6");
                            break;

                        case "7":
                            //NP
                            ps.setString(1, name);
                            ps.setString(2, price);
                            ps.setString(3, barcode);
                            System.out.println("Scenario 7");
                            break;

                        case "8":
                            //NS
                            ps.setString(1, name);
                            ps.setString(2, supplierId);
                            ps.setString(3, barcode);
                            System.out.println("Scenario 8");
                            break;

                        case "9":
                            //PQ
                            ps.setString(1, price);
                            ps.setString(2, quantity);
                            ps.setString(3, barcode);
                            System.out.println("Scenario 9");
                            break;

                        case "10":
                            //PS
                            ps.setString(1, price);
                            ps.setString(2, supplierId);
                            ps.setString(3, barcode);
                            System.out.println("Scenario 10");
                            break;

                        case "11":
                            //QS
                            ps.setString(1, name);
                            ps.setString(2, price);
                            ps.setString(3, quantity);
                            ps.setString(4, barcode);
                            System.out.println("Scenario 11");
                            break;

                        case "12":
                            //N
                            ps.setString(1, name);
                            ps.setString(2, barcode);
                            System.out.println("Scenario 12");
                            break;

                        case "13":
                            //P
                            ps.setString(1, price);
                            ps.setString(2, barcode);
                            System.out.println("Scenario 13");
                            break;

                        case "14":
                            //Q
                            ps.setString(1, quantity);
                            ps.setString(2, barcode);
                            System.out.println("Scenario 14");
                            break;

                        case "15":
                            //S
                            ps.setString(1, supplierId);
                            ps.setString(2, barcode);
                            System.out.println("Scenario 15");
                            break;

                        default:
                            System.out.println("Default");
                            return null;
                    }
                }

                output = ps.executeUpdate();

                if (output == 1) {
                    return 2; // update success
                } else {
                    return 0; // error message
                }
            } catch (SQLException e) {
                System.out.println("Error while running query");
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, null, ps, null);
            }
        }

        return output;
    }

    private Integer deleteProduct(String barcode) throws SQLException {
        Connection connection = db.establishConnection();
        query = "DELETE FROM product WHERE barcode = '"+barcode+"'";
        int output = -1;
        if(connection != null) {
            try {
                statement = connection.createStatement();
                output = statement.executeUpdate(query);

                if (output == 1) {
                    return 4; // deleted successfully
                } else {
                    return 5; // delete error
                }
            } catch (SQLException e) {
                System.out.println("Error while running query");
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, statement, null, null);
            }
        }

        return output;
    }

    private ObservableList<Product> searchProduct(String searchInput) throws SQLException {
        Connection connection = db.establishConnection();
        query = "SELECT * FROM product where product_name LIKE ? OR barcode LIKE ?" ;
        String wildcard = "%" + searchInput + "%";

        if(connection != null) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            try {
                preparedStatement.setString(1, wildcard);
                preparedStatement.setString(2, wildcard);

                resultSet = preparedStatement.executeQuery();
                data = convertData(resultSet);
            } catch (SQLException e) {
                System.out.println("Error while running query");
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, null, preparedStatement, resultSet);
            }
        }

        return data;
    }

    private static ObservableList<Product> convertData(ResultSet resultSet) throws SQLException{
        ObservableList<Product> productList = FXCollections.observableArrayList();

        try {
            while(resultSet.next()) {
                Product product = new Product();
                product.setBarcode(resultSet.getString("barcode"));
                product.setProductName(resultSet.getString("product_name"));
                product.setPrice(resultSet.getString("price"));
                product.setQuantity(resultSet.getInt("quantity"));
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
