package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Supplier;
import com.pos.inventorysystem.actions.SupplierActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierHelper {
    public static ObservableList<Supplier> getAllSupplierRecords(ResultSet resultSet) throws SQLException {
        try {
            return getAllSupplierList(resultSet);
        } catch (SQLException e) {
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }

    public static ResultSet getAllRecords() throws SQLException, ClassNotFoundException {
        SupplierActions actions = new SupplierActions();
        try{
            ResultSet resultSet = actions.getAllSuppliers();
            return resultSet;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error showing table");
            throw e;
        }
    }

    public static ResultSet getSearchedList(String input) throws ClassNotFoundException, SQLException {
        SupplierActions actions = new SupplierActions();
        try{
            ResultSet resultSet = actions.searchSupplier(input);
            return resultSet;
        } catch (SQLException | ClassNotFoundException e) {
            throw e;
        }
    }

    private static ObservableList<Supplier> getAllSupplierList(ResultSet resultSet) throws SQLException {
        try{
            ObservableList<Supplier> list = FXCollections.observableArrayList();
            while(resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(resultSet.getString("supplier_id"));
                supplier.setSupplierName(resultSet.getString("supplier_name"));
                supplier.setSupplierContactNo(resultSet.getString("contact"));
                supplier.setSupplierEmail(resultSet.getString("email"));
                supplier.setCompanyName(resultSet.getString("company_name"));

                list.add(supplier);
            }

            return list;
        } catch (SQLException e) {
            System.out.println("Error showing table");
            e.printStackTrace();
            throw e;
        }
    }
}
