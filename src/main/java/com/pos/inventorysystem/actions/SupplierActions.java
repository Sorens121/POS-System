package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.ErrorMsgHelper;

import java.sql.PreparedStatement;
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

    private ResultSet getOneSupplier(String supplierId) throws SQLException, ClassNotFoundException {
        query = "SELECT * FROM supplier WHERE supplier_id = '"+supplierId+"'";
        try{
            s = db.myConnection().createStatement();
            resultSet = s.executeQuery(query);
            return resultSet;
        } catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer createNewSupplier(String supplierId, String name, String contact, String email, String company) throws ClassNotFoundException, SQLException{
        int output = 0;
        query = "INSERT INTO supplier (supplier_id, supplier_name, contact, email, company_name) VALUES ('"+supplierId+"', '"+name+"', '"+contact+"', '"+email+"', '"+company+"')";
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

    public Integer updateSupplier(String id, String name, String contact, String email, String company) throws SQLException, ClassNotFoundException{
        query = "UPDATE supplier SET supplier_name ";
        int output = 0;

        ResultSet oldValues = getOneSupplier(id);

        String oldName = null, oldContact = null, oldEmail = null, oldCompany = null, state = null;

        while(oldValues.next()) {
            oldName = oldValues.getString("supplier_name");
            oldContact = oldValues.getString("contact");
            oldEmail = oldValues.getString("email");
            oldCompany = oldValues.getString("company_name");
        }

        // SCENARIOS
        //NCECm
        assert oldName != null;
        if(oldName.equalsIgnoreCase(name) && oldContact.equalsIgnoreCase(contact) && oldEmail.equalsIgnoreCase(email) && oldCompany.equalsIgnoreCase(company)) {
            query += "supplier_name = ?, contact = ?, email = ?, company_name = ? WHERE supplier_id = ?";
            state = "1";
        }
        //NECm
        else if(oldName.equalsIgnoreCase(name) && oldEmail.equalsIgnoreCase(email) && oldCompany.equalsIgnoreCase(company)) {
            query += "supplier_name = ?, email = ?, company_name = ? WHERE supplier_id = ?";
            state = "2";
        }
        //NCE
        else if(oldName.equalsIgnoreCase(name) && oldContact.equalsIgnoreCase(contact) && oldEmail.equalsIgnoreCase(email)) {
            query += "supplier_name = ?, contact = ?, email = ? WHERE supplier_id = ?";
            state = "3";
        }
        //NCCm
        else if(oldName.equalsIgnoreCase(name) && oldContact.equalsIgnoreCase(contact) && oldCompany.equalsIgnoreCase(company)) {
            query += "supplier_name = ?, contact = ?, company_name = ? WHERE supplier_id = ?";
            state = "4";
        }
        //CECm
        else if(oldContact.equalsIgnoreCase(contact) && oldEmail.equalsIgnoreCase(email) && oldCompany.equalsIgnoreCase(company)) {
            query += "contact = ?, email = ?, company_name = ? WHERE supplier_id = ?";
            state = "5";
        }
        //NC
        else if(oldName.equalsIgnoreCase(name) && oldContact.equalsIgnoreCase(contact)) {
            query += "supplier_name = ?, contact = ? WHERE supplier_id = ?";
            state = "6";
        }
        //NE
        else if(oldName.equalsIgnoreCase(name) && oldEmail.equalsIgnoreCase(email) ) {
            query += "supplier_name = ?, email = ? WHERE supplier_id = ?";
            state = "7";
        }
        //NCm
        else if(oldName.equalsIgnoreCase(name) && oldCompany.equalsIgnoreCase(company)) {
            query += "supplier_name = ?, company_name = ? WHERE supplier_id = ?";
            state = "8";
        }
        //CE
        else if(oldContact.equalsIgnoreCase(contact) && oldEmail.equalsIgnoreCase(email)) {
            query += "contact = ?, email = ? WHERE supplier_id = ?";
            state = "9";
        }
        //CCm
        else if(oldContact.equalsIgnoreCase(contact) && oldCompany.equalsIgnoreCase(company)) {
            query += "contact = ?, company_name = ? WHERE supplier_id = ?";
            state = "10";
        }
        //N
        else if(oldName.equalsIgnoreCase(name)) {
            query += "supplier_name = ? WHERE supplier_id = ?";
            state = "11";
        }
        //C
        else if(oldContact.equalsIgnoreCase(contact)) {
            query += "contact = ? WHERE supplier_id = ?";
            state = "12";
        }
        //E
        else if(oldEmail.equalsIgnoreCase(email)) {
            query += "email = ? WHERE supplier_id = ?";
            state = "13";
        }
        //Cm
        else if(oldCompany.equalsIgnoreCase(company)) {
            query += "company_name = ? WHERE supplier_id = ?";
            state = "14";
        }

        try(PreparedStatement ps = db.myConnection().prepareStatement(query)){
            if(state != null) {
                switch (state) {
                    case "1":
                        //NCECm
                        ps.setString(1, name);
                        ps.setString(2, contact);
                        ps.setString(3, email);
                        ps.setString(4, company);
                        ps.setString(5, id);
                        break;
                    case "2":
                        //NECm
                        ps.setString(1, name);
                        ps.setString(2, email);
                        ps.setString(3, company);
                        ps.setString(4, id);
                        break;
                    case "3":
                        //NCE
                        ps.setString(1, name);
                        ps.setString(2, contact);
                        ps.setString(3, email);
                        ps.setString(4, id);
                        break;
                    case "4":
                    //NCCm
                        ps.setString(1, name);
                        ps.setString(2, contact);
                        ps.setString(3, company);
                        ps.setString(4, id);
                        break;
                    case "5":
                    //CECm
                        ps.setString(1, contact);
                        ps.setString(2, email);
                        ps.setString(3, company);
                        ps.setString(4, id);
                        break;
                    case "6":
                    //NC
                        ps.setString(1, name);
                        ps.setString(2, contact);
                        ps.setString(3, id);
                        break;
                    case "7":
                    //NE
                        ps.setString(1, name);
                        ps.setString(2, email);
                        ps.setString(3, id);
                        break;
                    case "8":
                    //NCm
                        ps.setString(1, name);
                        ps.setString(2, company);
                        ps.setString(3, id);
                        break;
                    case "9":
                    //CE
                        ps.setString(1, contact);
                        ps.setString(2, email);
                        ps.setString(3, id);
                        break;
                    case "10":
                    //CCm
                        ps.setString(1, contact);
                        ps.setString(2, company);
                        ps.setString(3, id);
                        break;
                    case "11":
                    //N
                        ps.setString(1, name);
                        ps.setString(2, id);
                        break;
                    case "12":
                    //C
                        ps.setString(1, contact);
                        ps.setString(2, id);
                        break;
                    case "13":
                    //E
                        ps.setString(1, email);
                        ps.setString(2, id);
                        break;
                    case "14":
                    //Cm
                        ps.setString(1, company);
                        ps.setString(2, id);
                        break;

                    default:
                        return null;
                }
            }
            output = ps.executeUpdate();

            if(output == 1){
                return 2; // update successful
            } else {
                return 0; // update failed
            }
        } catch (SQLException e){
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            throw e;
        }
    }

    public Integer deleteSupplier(String id) throws ClassNotFoundException, SQLException {
        query = "DELETE FROM supplier WHERE supplier_id = '"+id+"'";
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


    public ResultSet searchSupplier(String searchInput) throws ClassNotFoundException, SQLException {
        query = "SELECT * FROM supplier WHERE supplier_name LIKE ? OR contact = ?";
        String wildcard = "%" + searchInput + "%";

        try{
            PreparedStatement preparedStatement = db.myConnection().prepareStatement(query);
            preparedStatement.setString(1, wildcard);
            preparedStatement.setString(2, searchInput);

            resultSet = s.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error while running the query: "+ e.getMessage());
            ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            throw e;
        }
    }
}
