package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.ErrorMsgHelper;

import java.sql.PreparedStatement;
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
            s = Objects.requireNonNull(db.myConnection()).createStatement();
            resultSet = s.executeQuery(sql);
            return resultSet;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while running query");
            e.printStackTrace();
            throw e;
        }

    }

    private ResultSet getOneCustomer(String customerId) throws SQLException, ClassNotFoundException {
        sql = "SELECT * FROM customer WHERE customer_id = '"+customerId+"'";
        try {
            s = db.myConnection().createStatement();
            resultSet = s.executeQuery(sql);
            return resultSet;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer createCustomer(String customerId, String name, String contact, String email) throws SQLException, ClassNotFoundException {
        int output = 0;
        sql = "INSERT INTO customer (customer_id, customer_name, contact_no, email) VALUES ('"+customerId+"', '"+name+"', '"+contact+"', '"+email+"')";
        try{
            s = db.myConnection().createStatement();
            output = s.executeUpdate(sql);
            return output;
        } catch (SQLException e) {
            System.out.println("Error while running query");
            ErrorMsgHelper.catchError(String.valueOf(e.getErrorCode()));
            throw e;
        }
    }

    public Integer updateCustomer(String customerId, String name, String contact, String email) throws SQLException, ClassNotFoundException {
        String updateQuery = "UPDATE customer SET ";
        int output = 0;

        ResultSet oldValues = getOneCustomer(customerId);

        String oldName = null, oldEmail = null, state = null, oldContact = null;

        while (oldValues.next()){
            oldName = oldValues.getString("customer_name");
            oldContact = oldValues.getString("contact_no");
            oldEmail = oldValues.getString("email");
        }

        // SCENARIOS 1
        //NCE
        assert oldName != null;
        if(!oldName.equalsIgnoreCase(name) && !oldContact.equalsIgnoreCase(contact) && !oldEmail.equalsIgnoreCase(email)) {
            updateQuery += "customer_name = ?, contact_no = ?, email = ? WHERE customer_id = ?";
            state = "1";
        }
        //NC
        else if (!oldName.equalsIgnoreCase(name) && !oldContact.equalsIgnoreCase(contact)) {
            updateQuery += "customer_name = ?, contact_no = ? WHERE customer_id = ?";
            state = "2";
        }
        //NE
        else if (!oldName.equalsIgnoreCase(name) && !oldEmail.equalsIgnoreCase(email)) {
            updateQuery += "customer_name = ?, email = ? WHERE customer_id = ?";
            state = "3";
        }
        //CE
        else if (!oldContact.equalsIgnoreCase(contact) && !oldEmail.equalsIgnoreCase(email)) {
            updateQuery += "contact_no = ?, email = ? WHERE customer_id = ?";
            state = "4";
        }
        //N
        else if (!oldName.equalsIgnoreCase(name)) {
            updateQuery += "contact_name = ? WHERE customer_id = ?";
            state = "5";
        }
        //C
        else if (!oldContact.equalsIgnoreCase(contact)) {
            updateQuery += "contact_no = ? WHERE customer_id = ?";
            state = "6";
        }
        //E
        else if (!oldEmail.equalsIgnoreCase(email)) {
            updateQuery += "email = ? WHERE customer_id = ?";
            state = "7";
        }


        try(PreparedStatement ps = db.myConnection().prepareStatement(updateQuery)){
            if(state != null){
              switch (state) {
                  case "1":
                      //NCE
                      ps.setString(1, name);
                      ps.setString(2, contact);
                      ps.setString(3, email);
                      ps.setString(4, customerId);
                      break;

                  case "2":
                      //NC
                      ps.setString(1, name);
                      ps.setString(2, contact);
                      ps.setString(3, customerId);
                      break;

                  case "3":
                      //NE
                      ps.setString(1, name);
                      ps.setString(2, email);
                      ps.setString(3, customerId);
                      break;

                  case "4":
                      //CE
                      ps.setString(1, contact);
                      ps.setString(2, email);
                      ps.setString(3, customerId);
                      break;

                  case "5":
                      //N
                      ps.setString(1,name);
                      ps.setString(2,customerId);
                      break;

                  case "6":
                      //C
                      ps.setString(1, contact);
                      ps.setString(2, customerId);
                      break;

                  case "7":
                      //E
                      ps.setString(1, email);
                      ps.setString(2, customerId);
                      break;

                  default:
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
            throw e;
        }
    }


    public Integer deleteCustomer(String customerId) throws SQLException, ClassNotFoundException {
        sql = "DELETE FROM customer WHERE customer_id = '"+customerId+"'";
        try {
            s = Objects.requireNonNull(db.myConnection()).createStatement();
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

    public ResultSet searchCustomer(String searchInput) throws SQLException, ClassNotFoundException {
        sql = "SELECT * FROM customer WHERE customer_name LIKE ? OR email LIKE ? OR contact_no = ?";
        String wildcard = "%" + searchInput + "%";

        try{
            PreparedStatement ps = db.myConnection().prepareStatement(sql);
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            ps.setString(3, searchInput);

            resultSet = ps.executeQuery();
            return resultSet;
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Error while running query");
            e.printStackTrace();
            throw e;
        }
    }
}
