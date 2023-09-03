package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.ErrorMsgHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class CustomerActions {

    private Statement s;
    private ResultSet resultSet = null;
    private String sql = null;

    public ResultSet getAllCustomers() throws SQLException, ClassNotFoundException {
        sql = "SELECT * FROM customer";
        try {
            s = Objects.requireNonNull(db.myconnect()).createStatement();
            resultSet = s.executeQuery(sql);
            return resultSet;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while running query");
            e.printStackTrace();
            throw e;
        }

    }

    public Integer createCustomer(String name, String tp) throws SQLException, ClassNotFoundException {
        int output = 0;
        sql = "INSERT INTO customer (customer_name, Tp_Number) VALUES ('"+name+"', '"+tp+"')";
        try{
            s = Objects.requireNonNull(db.myconnect()).createStatement();
            output = s.executeUpdate(sql);
            return output;
        } catch (SQLException e) {
            System.out.println("Error while running query");
            ErrorMsgHelper.catchError(String.valueOf(e.getErrorCode()));
            throw e;
        }
    }

    public Integer updateCustomer(String id, String name, String tp) throws SQLException, ClassNotFoundException {
        sql = "UPDATE customer SET customer_name = '"+name+"', Tp_Number = '"+tp+"' WHERE cid = '"+id+"'";
        int output = 0;
        try{
            s = Objects.requireNonNull(db.myconnect()).createStatement();
            output = s.executeUpdate(sql);
            if(output == 1){
                return 2; // update success
            } else {
                return 0; // error message
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while running query");
            e.printStackTrace();
            throw e;
        }
    }

    public Integer deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        sql = "DELETE FROM customer WHERE cid = '"+id+"'";
        try {
            s = Objects.requireNonNull(db.myconnect()).createStatement();
            int output = s.executeUpdate(sql);
            if(output == 1) {
                return 4; // delete success
            } else {
                return 5; // delete error
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while running query");
            e.printStackTrace();
            throw e;
        }
    }

    public ResultSet searchCustomerById(String id) throws SQLException, ClassNotFoundException {
        sql = "SELECT * FROM customer WHERE cid = '"+id+"'";
        try{
            s = Objects.requireNonNull(db.myconnect()).createStatement();
            resultSet = s.executeQuery(sql);
            return resultSet;
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Error while running query");
            e.printStackTrace();
            throw e;
        }
    }

    public ResultSet searchCustomerByDetails(String name, String tp) throws SQLException, ClassNotFoundException {
        if(!name.isEmpty()){
            if(name.length() < 3){
                //follow pattern
                char pattern = name.charAt(0);
                String res = String.valueOf(pattern);
                sql = "SELECT * FROM customer WHERE customer_name LIKE '"+res+"' \"%\"";
            } else {
                sql = "SELECT * FROM customer WHERE customer_name = '"+name+"'";
            }
        } else if(!tp.isEmpty()) {
            sql = "SELECT * FROM customer WHERE Tp_Number = '"+tp+"'";
        }

        try{
            s = Objects.requireNonNull(db.myconnect()).createStatement();
            resultSet = s.executeQuery(sql);
            return resultSet;
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Error while running query");
            e.printStackTrace();
            throw e;
        }

    }
}
