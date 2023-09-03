package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.ErrorMsgHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeActions {
    private Statement s;
    private ResultSet resultSet = null;
    private String query = null;
    public ResultSet getAllEmployees() throws SQLException, ClassNotFoundException{
        query = "SELECT * FROM employee";
        try{
            s = db.myconnect().createStatement();
            resultSet = s.executeQuery(query);
            return resultSet;
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Error while running the query: "+ e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Integer createNewEmployee(String name, String employee_tp) throws ClassNotFoundException, SQLException{
        int output = 0;
        query = "INSERT INTO employee (employee_name, employee_Tp_Number) VALUES ('"+name+"', '"+employee_tp+"')";
        try{
            s = db.myconnect().createStatement();
            output = s.executeUpdate(query);
            return output;
        } catch (SQLException e){
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError(String.valueOf(e.getErrorCode()));
            throw e;
        }
    }

    public Integer updateEmployee(String id, String name, String employee_tp) throws SQLException, ClassNotFoundException{
        query = "UPDATE employee SET employee_name = '"+name+"', employee_Tp_Number = '"+employee_tp+"' WHERE eid = '"+id+"'";
        try{
            s = db.myconnect().createStatement();
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

    public Integer deleteEmployee(String id) throws ClassNotFoundException, SQLException {
        query = "DELETE FROM employee WHERE eid = '"+id+"'";
        try{
            s = db.myconnect().createStatement();
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

    public ResultSet searchEmployeeById(String id) throws ClassNotFoundException, SQLException {
        query = "SELECT * FROM employee WHERE eid = '"+id+"'";
        try{
            s = db.myconnect().createStatement();
            resultSet = s.executeQuery(query);
            return resultSet;
        } catch(SQLException e) {
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            throw e;
        }
    }

    public ResultSet searchEmployeeByDetails(String name, String employee_tp) throws ClassNotFoundException, SQLException {
        if(!name.isEmpty()){
            if(name.length() < 3) {
                //follow pattern
                char pattern = name.charAt(0);
                String res = String.valueOf(pattern);
                query = "SELECT * FROM employee WHERE employee_name LIKE '"+res+"' \"%\"";
            } else {
                query = "SELECT * FROM employee WHERE employee_name = '"+name+"'";
            }
        } else if(!employee_tp.isEmpty()){
            query = "SELECT * FROM employee WHERE employee_Tp_Number = '"+employee_tp+"'";
        }
        try{
            s = db.myconnect().createStatement();
            resultSet = s.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            throw e;
        }
    }
}
