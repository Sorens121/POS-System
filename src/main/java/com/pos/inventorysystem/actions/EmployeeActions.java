package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.ErrorMsgHelper;

import java.sql.PreparedStatement;
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
            s = db.myConnection().createStatement();
            resultSet = s.executeQuery(query);
            return resultSet;
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Error while running the query: "+ e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private ResultSet getOneEmployee(String employeeId) {
        query = "SELECT * FROM employee WHERE employee_id = '"+employeeId+"'";
        try{
            s = db.myConnection().createStatement();
            resultSet = s.executeQuery(query);
            return resultSet;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer createNewEmployee(String employeeId, String name, String contact, String email) throws ClassNotFoundException, SQLException{
        int output = 0;
        query = "INSERT INTO employee (employee_id, employee_name, contact_no, email) VALUES ('"+employeeId+"', '"+name+"', '"+contact+"', '"+email+"')";
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

    public Integer updateEmployee(String employeeId, String name, String contact, String email) throws SQLException, ClassNotFoundException{
        String updateQuery = "UPDATE employee SET ";
        int output = 0;

        ResultSet oldValues = getOneEmployee(employeeId);

        String oldName = null, oldContact = null, oldEmail = null, state = null;

        while(oldValues.next()) {
            oldName = oldValues.getString("employee_name");
            oldContact = oldValues.getString("contact");
            oldEmail = oldValues.getString("email");
        }

        // SCENARIOS 1
        //NCE
        assert oldName != null;
        if(!oldName.equalsIgnoreCase(name) && !oldContact.equalsIgnoreCase(contact) && !oldEmail.equalsIgnoreCase(email)){
            updateQuery += "employee_name = ?, contact = ?, email = ? WHERE employee_id = ?";
            state = "1";
        }
        //NC
        else if (!oldName.equalsIgnoreCase(name) && !oldContact.equalsIgnoreCase(contact)) {
            updateQuery += "employee_name = ?, contact = ? WHERE employee_id = ?";
            state = "2";
        }
        //NE
        else if (!oldName.equalsIgnoreCase(name) && !oldEmail.equalsIgnoreCase(email)) {
            updateQuery += "employee_name = ?, email = ? WHERE employee_id = ?";
            state = "3";
        }
        //CE
        else if (!oldContact.equalsIgnoreCase(contact) && !oldEmail.equalsIgnoreCase(email)) {
            updateQuery += "contact = ?, email = ? WHERE employee_id = ?";
            state = "4";
        }
        //N
        else if (!oldName.equalsIgnoreCase(name)) {
            updateQuery += "employee_name = ? WHERE employee_id = ?";
            state = "5";
        }
        //C
        else if (!oldContact.equalsIgnoreCase(contact)) {
            updateQuery += "contact = ?, WHERE employee_id = ?";
            state = "6";
        }
        //E
        else if (!oldEmail.equalsIgnoreCase(email)) {
            updateQuery += "email = ? WHERE employee_id = ?";
            state = "7";
        }
        try(PreparedStatement ps = db.myConnection().prepareStatement(updateQuery)){
            if(state != null){
                switch (state){
                    case "1":
                        ps.setString(1, name);
                        ps.setString(2, contact);
                        ps.setString(3, email);
                        ps.setString(4, employeeId);
                        break;
                    case "2":
                        ps.setString(1, name);
                        ps.setString(2, contact);
                        ps.setString(3, employeeId);
                        break;

                    case "3":
                        ps.setString(1, name);
                        ps.setString(2, email);
                        ps.setString(4, employeeId);
                        break;

                    case "4":
                        ps.setString(1, contact);
                        ps.setString(2, email);
                        ps.setString(3, employeeId);
                        break;

                    case "5":
                        ps.setString(1, name);
                        ps.setString(2, employeeId);
                        break;

                    case "6":
                        ps.setString(1, contact);
                        ps.setString(2, employeeId);
                        break;

                    case "7":
                        ps.setString(1, email);
                        ps.setString(2, employeeId);
                        break;

                    default:
                        return null;
                }
            }

            output = ps.executeUpdate();

            if(output == 1){
                return 2; // update message
            } else {
                return 0; // failed update message
            }
        } catch (SQLException e){
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            throw e;
        }
    }



    public Integer deleteEmployee(String employeeId) throws ClassNotFoundException, SQLException {
        query = "DELETE FROM employee WHERE employee_id = '"+employeeId+"'";
        try{
            s = db.myConnection().createStatement();
            int output = s.executeUpdate(query);
            if(output == 1){
                return 4; // delete success
            } else {
                return 5; // failed delete
            }
        } catch(SQLException e) {
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            throw e;
        }
    }

    public ResultSet searchEmployee(String searchInput) throws SQLException, ClassNotFoundException {
        query = "SELECT * FROM employee WHERE employee_name LIKE ? OR email LIKE ? OR contact_no LIKE ? OR employee_id = ?";
        String wildcard = "%" + searchInput + "%";

        try{
            PreparedStatement preparedStatement = db.myConnection().prepareStatement(query);
            preparedStatement.setString(1, wildcard);
            preparedStatement.setString(2, wildcard);
            preparedStatement.setString(3, wildcard);
            preparedStatement.setString(4, searchInput);

            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while running the query");
            e.printStackTrace();
            throw e;
        }
    }
}
