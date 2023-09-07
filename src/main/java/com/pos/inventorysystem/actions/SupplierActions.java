package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.ErrorMsgHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SupplierActions {
    private Statement s;
    private ResultSet resultSet = null;
    private String query = null;

    public ResultSet getAllSuppliers() throws SQLException, ClassNotFoundException{
        query = "SELECT * FROM supplier";
        try{
            s = db.myConnection().createStatement();
            resultSet = s.executeQuery(query);
            return resultSet;
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Error while running the query: "+ e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Integer createNewSupplier(String name, String supplier_tp) throws ClassNotFoundException, SQLException{
        int output = 0;
        query = "INSERT INTO supplier (supplier_name, supplier_Tp_Number) VALUES ('"+name+"', '"+supplier_tp+"')";
        try{
            s = db.myConnection().createStatement();
            output = s.executeUpdate(query);
            return output;
        } catch (SQLException e){
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError(String.valueOf(e.getErrorCode()));
            throw e;
        }
    }

    public Integer updateSupplier(String id, String name, String supplier_tp) throws SQLException, ClassNotFoundException{
        query = "UPDATE supplier SET supplier_name = '"+name+"', supplier_Tp_Number = '"+supplier_tp+"' WHERE sid = '"+id+"'";
        try{
            s = db.myConnection().createStatement();
            int output = s.executeUpdate(query);
            if(output == 1){
                return 2;
            } else {
                return 0;
            }
        } catch (SQLException e){
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            throw e;
        }
    }

    public Integer deleteSupplier(String id) throws ClassNotFoundException, SQLException {
        query = "DELETE FROM supplier WHERE sid = '"+id+"'";
        try{
            s = db.myConnection().createStatement();
            int output = s.executeUpdate(query);
            if(output == 1){
                return 4;
            } else {
                return 5;
            }
        } catch(SQLException e) {
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            throw e;
        }
    }

    public ResultSet searchSupplierById(String id) throws ClassNotFoundException, SQLException {
        query = "SELECT * FROM supplier WHERE sid = '"+id+"'";
        try{
            s = db.myConnection().createStatement();
            resultSet = s.executeQuery(query);
            return resultSet;
        } catch(SQLException e) {
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            throw e;
        }
    }

    public ResultSet searchSupplierByDetails(String name, String supplier_tp) throws ClassNotFoundException, SQLException {
        if(!name.isEmpty()){
            if(name.length() < 3) {
                //follow pattern
                char pattern = name.charAt(0);
                String res = String.valueOf(pattern);
                query = "SELECT * FROM supplier WHERE supplier_name LIKE '"+res+"' \"%\"";
            } else {
                query = "SELECT * FROM supplier WHERE supplier_name = '"+name+"'";
            }
        } else if(!supplier_tp.isEmpty()){
            query = "SELECT * FROM supplier WHERE supplier_Tp_Number = '"+supplier_tp+"'";
        }
        try{
            s = db.myConnection().createStatement();
            resultSet = s.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            throw e;
        }
    }
}
