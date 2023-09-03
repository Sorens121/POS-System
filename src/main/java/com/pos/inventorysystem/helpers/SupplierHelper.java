package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Supplier;
import com.pos.inventorysystem.actions.SupplierActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierHelper {
    public static ObservableList<Supplier> getAllRecords() throws ClassNotFoundException, SQLException {
        SupplierActions actions = new SupplierActions();
        try {
            ResultSet resultSet = actions.getAllSuppliers();
            ObservableList<Supplier> list = getAllSupplierList(resultSet);
            return list;
        } catch (SQLException e) {
            System.out.println("Error showing table: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static ObservableList<Supplier> getSearchedList(String id) throws ClassNotFoundException, SQLException{
        SupplierActions actions = new SupplierActions();
        try{
            ResultSet resultSet = actions.searchSupplierById(id);
            ObservableList<Supplier> list = getAllSupplierList(resultSet);
            return list;
        } catch(SQLException e){
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }

    public static ObservableList<Supplier> getSearchedListByDetails(String name, String tp) throws ClassNotFoundException, SQLException{
        SupplierActions actions = new SupplierActions();
        try{
            ResultSet resultSet = actions.searchSupplierByDetails(name, tp);
            ObservableList<Supplier> list = getAllSupplierList(resultSet);
            return list;
        } catch(SQLException |ClassNotFoundException e){
            System.out.println("Error while showing table");
            e.printStackTrace();
            throw e;
        }
    }

    private static ObservableList<Supplier> getAllSupplierList(ResultSet resultSet) throws ClassNotFoundException, SQLException {
        try{
            ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
            while(resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(resultSet.getString("sid"));
                supplier.setSupplierName(resultSet.getString("supplier_name"));
                supplier.setSupplierTpNo(resultSet.getString("supplier_Tp_Number"));
                supplierList.add(supplier);
            }

            return supplierList;
        } catch(SQLException e) {
            System.out.println("Error showing table: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
