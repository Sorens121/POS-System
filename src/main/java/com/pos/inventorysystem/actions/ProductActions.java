package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.ErrorMsgHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductActions {
    private Statement s;
    private ResultSet resultSet = null;
    private String query = null;

    public ResultSet getAllProducts() throws SQLException, ClassNotFoundException{
        query = "SELECT * FROM product";

        try{
            s = db.myconnect().createStatement();
            resultSet = s.executeQuery(query);
            return resultSet;
        } catch(SQLException | ClassNotFoundException e) {
            System.out.println("Error while running query");
            e.printStackTrace();
            throw e;
        }
    }

    public ResultSet getOneProduct(String searchID) throws SQLException, ClassNotFoundException {
        query = "SELECT * FROM product WHERE barcode = '"+searchID+"'";
        try{
            s = db.myconnect().createStatement();
            resultSet = s.executeQuery(query);
            return resultSet;
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public Integer addNewProduct(String name, String barcode, int price, int quantity, String supplierId) throws SQLException, ClassNotFoundException{
        int output = 0;
        query = "INSERT INTO product (product_name, barcode, price, quantity, supplier_id) VALUES ('"+name+"', '"+barcode+"', '"+price+"', '"+quantity+"', '"+supplierId+"')";
        try {
            s = db.myconnect().createStatement();
            output = s.executeUpdate(query);
            return output;
        } catch (SQLException e) {
            System.out.println("Error while running query");
            ErrorMsgHelper.catchError(String.valueOf(e.getErrorCode()));
            throw e;
        }
    }

    public Integer updateProduct(String barcode, String name, int price, int quantity, String supplierId) throws SQLException, ClassNotFoundException {
        int output = 0;
        String updateQuery = "UPDATE product SET ";

        ResultSet oldValue = getOneProduct(barcode);
        String oldName = null, oldSupplierId = null, state = null;
        int oldPrice = 0, oldQuantity = 0;
        while(oldValue.next()){
            oldName = oldValue.getString("product_name");
            oldPrice = oldValue.getInt("price");
            oldQuantity = oldValue.getInt("quantity");
            oldSupplierId = oldValue.getString("supplier_id");
        }

        // scenarios - 1
//        if (!name.isEmpty() && name != null) { // UPDATING ONLY PRODUCT NAME
//            updateQuery += "product_name = ? ";
//            System.out.println("Scenario 1");
//        } else if (price != 0) { // UPDATING ONL`Y PRICE
//            updateQuery += "price = ? ";
//            System.out.println("Scenario 2");
//        } else if (quantity != 0) { // UPDATING ONLY QUANTITY
//            updateQuery += "quantity = ?";
//            System.out.println("Scenario 3");
//        } else if (!supplierId.isEmpty() && supplierId != null) { //UPDATING ONLY SELLER ID
//            updateQuery += "supplier_id = ?";
//            System.out.println("Scenario 4");
//        } else if ((!name.isEmpty() && name != null) && price != 0) { //UPDATING NAME AND PRICE
//            updateQuery += "product_name = ?, price = ?";
//            System.out.println("Scenario 5");
//        } else if ((!name.isEmpty() && name != null) && quantity != 0) { //UPDATING NAME AND QUANTITY
//            updateQuery += "product_name = ?, quantity = ? ";
//            System.out.println("Scenario 6");
//        } else if ((!name.isEmpty() && name != null) && (!supplierId.isEmpty() && supplierId != null)) { //UPDATING NAME AND SELLER ID
//            updateQuery += "product_name = ?, supplier_id = ? ";
//            System.out.println("Scenario 7");
//        } else if (price != 0 && quantity != 0) { //UPDATING PRICE AND QUANTITY
//            updateQuery += "price = ?, quantity = ? ";
//            System.out.println("Scenario 8");
//        } else if ((!supplierId.isEmpty() && supplierId != null) && price != 0) { //UPDATING PRICE AND SELLER ID
//            updateQuery += "price = ?, supplier_id = ? ";
//            System.out.println("Scenario 9");
//        } else if (((!supplierId.isEmpty() && supplierId != null) && quantity != 0)) { //UPDATING QUANTITY AND SELLER ID
//            updateQuery += "quantity = ?, supplier_id = ? ";
//            System.out.println("Scenario 10");
//        } else if ((!name.isEmpty() && name != null) && price != 0 && quantity != 0) { //UPDATING NAME PRICE AND QUANTITY
//            updateQuery += "product_name = ?, price = ?, quantity = ? ";
//            System.out.println("Scenario 11");
//        } else if ((!name.isEmpty() && name != null) && price != 0 && (!supplierId.isEmpty() && supplierId != null)) { //UPDATING NAME PRICE AND SELLER ID
//            updateQuery += "product_name = ?, price = ?, quantity = ? ";
//            System.out.println("Scenario 12");
//        } else if ((!supplierId.isEmpty() && supplierId != null) && price != 0 && quantity != 0) { //UPDATING PRICE QUANTITY AND SELLER ID
//            updateQuery += "product_name = ?, price = ?, quantity = ? ";
//            System.out.println("Scenario 13");
//        } else if ((!name.isEmpty() && name != null) && (!supplierId.isEmpty() && supplierId != null) && price != 0 && quantity != 0) { //UPDATING NAME PRICE QUANTITY AND SELLER ID
//            updateQuery += "product_name = ?, price = ?, quantity = ?, supplier_id = ? ";
//            System.out.println("Scenario 14");
//        }
//        try(PreparedStatement ps = db.myconnect().prepareStatement(updateQuery)){
//            if(!name.isEmpty() && name !=null){ // UPDATING ONLY PRODUCT NAME
//                ps.setString(1, name);
//                System.out.println("Scenario 1");
//
//            } else if(price != 0) { // UPDATING ONLY PRICE
//                ps.setInt(1, price);
//                System.out.println("Scenario 2");
//
//            } else if(quantity != 0) { // UPDATING ONLY QUANTITY
//                ps.setInt(1, quantity);
//                System.out.println("Scenario 3");
//
//            } else if(!supplierId.isEmpty() && supplierId != null){ //UPDATING ONLY SELLER ID
//                ps.setString(1, supplierId);
//                System.out.println("Scenario 4");
//
//            } else if((!name.isEmpty() && name != null) && price != 0){ //UPDATING NAME AND PRICE
//                ps.setString(1, name);
//                ps.setInt(2, price);
//                System.out.println("Scenario 5");
//
//            } else if((!name.isEmpty() && name != null) && quantity != 0){ //UPDATING NAME AND QUANTITY
//                ps.setString(1, name);
//                ps.setInt(2, quantity);
//                System.out.println("Scenario 6");
//
//            } else if((!name.isEmpty() && name != null) && (!supplierId.isEmpty() && supplierId != null)){ //UPDATING NAME AND SELLER ID
//                ps.setString(1, name);
//                ps.setString(2, supplierId);
//                System.out.println("Scenario 7");
//
//            } else if(price != 0 && quantity != 0 ){ //UPDATING PRICE AND QUANTITY
//                ps.setInt(1, price);
//                ps.setInt(2, quantity);
//                System.out.println("Scenario 8");
//
//            } else if((!supplierId.isEmpty() && supplierId != null) && price != 0){ //UPDATING PRICE AND SELLER ID
//                ps.setInt(1, price);
//                ps.setString(2, supplierId);
//                System.out.println("Scenario 9");
//
//            } else if(((!supplierId.isEmpty() && supplierId != null) && quantity != 0)){ //UPDATING QUANTITY AND SELLER ID
//                ps.setInt(1, quantity);
//                ps.setString(2, supplierId);
//                System.out.println("Scenario 10");
//
//            } else if ((!name.isEmpty() && name != null) && price != 0 && quantity != 0) { //UPDATING NAME PRICE AND QUANTITY
//                ps.setString(1, name);
//                ps.setInt(2, price);
//                ps.setInt(3, quantity);
//                System.out.println("Scenario 11");
//
//            } else if ((!name.isEmpty() && name != null) && price != 0 && (!supplierId.isEmpty() && supplierId != null)) { //UPDATING NAME PRICE AND SELLER ID
//                ps.setString(1, name);
//                ps.setInt(2, price);
//                ps.setString(3, supplierId);
//                System.out.println("Scenario 12");
//
//            } else if ((!supplierId.isEmpty() && supplierId != null) && price != 0 && quantity != 0) { //UPDATING PRICE QUANTITY AND SELLER ID
//                ps.setInt(1, price);
//                ps.setInt(2, quantity);
//                ps.setString(3, supplierId);
//                System.out.println("Scenario 13");
//
//            } else if ((!name.isEmpty() && name != null) && (!supplierId.isEmpty() && supplierId != null) && price != 0 && quantity != 0) { //UPDATING NAME PRICE QUANTITY AND SELLER ID
//                ps.setString(1, name);
//                ps.setInt(2, price);
//                ps.setInt(3, quantity);
//                ps.setString(4, supplierId);
//                System.out.println("Scenario 14");
//
//            }

        //Scenarios 2
        //NPQS

        assert oldName != null;
        //NPQS
        if(!oldName.equalsIgnoreCase(name) && oldPrice != price && oldQuantity != quantity && !oldSupplierId.equalsIgnoreCase(supplierId)){
            updateQuery += "product_name = ?, price = ?, quantity = ?, supplier_id = ? WHERE barcode = ?";
            state = "1";
        }
        //NPQ
        else if(!oldName.equalsIgnoreCase(name) && oldPrice != price && oldQuantity != quantity){
            updateQuery += "product_name = ?, price = ?, quantity = ? WHERE barcode = ?";
            state = "2";
        }
        //NPS
        else if(!oldName.equalsIgnoreCase(name) && oldPrice != price && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "product_name = ?, price = ?, supplier_id = ? WHERE barcode = ?";
            state = "3";
        }
        //NQS
        else if(!oldName.equalsIgnoreCase(name) && oldQuantity != quantity && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "product_name = ?, quantity = ?, supplier_id = ? WHERE barcode = ?";
            state = "4";
        }
        //PQS
        else if(oldPrice != price && oldQuantity != quantity && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "price = ?, quantity = ?, supplier_id = ? WHERE barcode = ?";
            state = "5";
        }
        //NQ
        else if(!oldName.equalsIgnoreCase(name) && oldQuantity != quantity ) {
            updateQuery += "product_name = ?, quantity = ? WHERE barcode = ?";
            state = "6";
        }
        //NP
        else if(!oldName.equalsIgnoreCase(name) && oldPrice != price ) {
            updateQuery += "product_name = ?, price = ? WHERE barcode = ?";
            state = "7";
        }
        //NS
        else if(!oldName.equalsIgnoreCase(name) && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "product_name = ?, supplier_id = ? WHERE barcode = ?";
            state = "8";
        }
        //PQ
        else if(oldPrice != price && oldQuantity != quantity) {
            updateQuery += "price = ?, quantity = ? WHERE barcode = ?";
            state = "9";
        }
        //PS
        else if(oldPrice != price && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "price = ?, supplier_id = ? WHERE barcode = ?";
            state = "10";
        }
        //QS
        else if(oldQuantity != quantity && !oldSupplierId.equalsIgnoreCase(supplierId)) {
            updateQuery += "quantity = ?, supplier_id = ? WHERE barcode = ?";
            state = "11";
        }
        //N
        else if(!oldName.equalsIgnoreCase(name)){
            updateQuery += "product_name = ? WHERE barcode = ?";
            state = "12";
        }
        //P
        else if(oldPrice != price){
            updateQuery += "price = ? WHERE barcode = ?";
            state = "13";
        }
        //Q
        else if(oldQuantity != quantity){
            updateQuery += "quantity = ? WHERE barcode = ?";
            state = "14";
        }
        //S
        else if(!oldSupplierId.equalsIgnoreCase(supplierId)){
            updateQuery += "supplier_id = ? WHERE barcode = ?";
            state = "15";
        }

        try(PreparedStatement ps = db.myconnect().prepareStatement(updateQuery)){
            if (state != null) {
                switch (state){
                    case "1":
                        ps.setString(1, name);
                        ps.setInt(2, price);
                        ps.setInt(3, quantity);
                        ps.setString(4, supplierId);
                        ps.setString(5, barcode);
                        System.out.println("Scenario 1");
                        break;
                    case "2":
                        //NPQ
                        ps.setString(1, name);
                        ps.setInt(2, price);
                        ps.setInt(3, quantity);
                        ps.setString(4, barcode);
                        System.out.println("Scenario 2");
                        break;
                    case "3":
                        //NPS
                        ps.setString(1, name);
                        ps.setInt(2, price);
                        ps.setString(3, supplierId);
                        ps.setString(4, barcode);
                        System.out.println("Scenario 3");
                        break;

                    case "4":
                        //NQS
                        ps.setString(1, name);
                        ps.setInt(2, quantity);
                        ps.setString(3, supplierId);
                        ps.setString(4, barcode);
                        System.out.println("Scenario 4");
                        break;

                    case "5":
                        //PQS
                        ps.setInt(1, price);
                        ps.setInt(2, quantity);
                        ps.setString(3, supplierId);
                        ps.setString(4, barcode);
                        System.out.println("Scenario 5");
                        break;

                    case "6":
                        //NQ
                        ps.setString(1, name);
                        ps.setInt(2, quantity);
                        ps.setString(3, barcode);
                        System.out.println("Scenario 6");
                        break;

                    case "7":
                        //NP
                        ps.setString(1, name);
                        ps.setInt(2, price);
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
                        ps.setInt(1, price);
                        ps.setInt(2, quantity);
                        ps.setString(3, barcode);
                        System.out.println("Scenario 9");
                        break;

                    case "10":
                        //PS
                        ps.setInt(1, price);
                        ps.setString(2, supplierId);
                        ps.setString(3, barcode);
                        System.out.println("Scenario 10");
                        break;

                    case "11":
                        //QS
                        ps.setString(1, name);
                        ps.setInt(2, price);
                        ps.setInt(3, quantity);
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
                        ps.setInt(1, price);
                        ps.setString(2, barcode);
                        System.out.println("Scenario 13");
                        break;

                    case "14":
                        //Q
                        ps.setInt(1, quantity);
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

            if(output == 1){
                return 2; // update success
            } else {
                return 0; // error message
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while running query");
            e.printStackTrace();
            return null;
        }
    }

    public Integer deleteProduct(String id) throws SQLException, ClassNotFoundException {
        query = "DELETE FROM product WHERE pid = '"+id+"'";
        try{
            s = db.myconnect().createStatement();
            int output = s.executeUpdate(query);
            if(output == 1) {
                return 4; // deleted successfully
            } else {
                return 5; // delete error
            }
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Error while running query");
            e.printStackTrace();
            throw e;
        }
    }

    public ResultSet searchProduct(String searchInput) throws SQLException, ClassNotFoundException {
        query = "SELECT * FROM product where product_name LIKE ? OR barcode LIKE ?" ;
        String wildcard = "%" + searchInput + "%";
        try{
            PreparedStatement preparedStatement = db.myconnect().prepareStatement(query);
            preparedStatement.setString(1, wildcard);
            preparedStatement.setString(2, wildcard);

            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while running query");
            e.printStackTrace();
            throw e;
        }
    }
}
