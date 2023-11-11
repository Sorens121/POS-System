package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.Model.Supplier;
import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.ErrorMsgHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SupplierActions {
    private Statement statement;
    private ResultSet resultSet = null;
    private ObservableList<Supplier> data = null;

    public ObservableList<Supplier> getAllRecords() throws SQLException {
        try {
            return getAllSuppliers();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<Supplier> getOneRecord(String id) throws SQLException {
        try{
            return getOneSupplier(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer createNewRecord(String supplierId, String name, String contact, String email, String company) throws SQLException {
        try{
            return createNewSupplier(supplierId, name, contact, email, company);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer updateRecord(String id, String name, String contact, String email, String company) throws SQLException {
        try{
            return updateSupplier(id, name, contact, email, company);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer deleteRecord(String id) throws SQLException {
        try{
            return deleteSupplier(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<Supplier> searchRecord(String searchInput) throws SQLException {
        try {
            return searchSupplier(searchInput);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private ObservableList<Supplier> getAllSuppliers() throws SQLException {
        Connection connection = db.establishConnection();
        String query = "SELECT * FROM supplier";

        if(connection != null) {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                data = convertData(resultSet);
            } catch (SQLException e) {
                System.out.println("Error while running the query: " + e.getMessage());
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, statement, null, resultSet);
            }
        }

        return data;
    }

    private ObservableList<Supplier> getOneSupplier(String supplierId) throws SQLException {
        Connection connection = db.establishConnection();
        String query = "SELECT * FROM supplier WHERE supplier_id = '"+supplierId+"'";

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

    private Integer createNewSupplier(String supplierId, String name, String contact, String email, String company) throws SQLException {
        Connection connection = db.establishConnection();
        String query = "INSERT INTO supplier (supplier_id, supplier_name, contact, email, company_name) VALUES ('"+supplierId+"', '"+name+"', '"+contact+"', '"+email+"', '"+company+"')";
        int output = -1;

        if(connection != null) {
            try {
                statement = connection.createStatement();
                output = statement.executeUpdate(query);
                return output;
            } catch (SQLException e) {
                System.out.println("Error while running the query: " + e.getMessage());
                ErrorMsgHelper.catchError(String.valueOf(e.getErrorCode()));
            } finally {
                db.closeConnection(connection, statement, null, null);
            }
        }
        return output;
    }

    private Integer updateSupplier(String id, String name, String contact, String email, String company) throws SQLException {
        Connection connection = db.establishConnection();
        String query = "UPDATE supplier SET ";
        int output = -1, index = 0;

        ObservableList<Supplier> oldValues = getOneSupplier(id);

        String oldName = null, oldContact = null, oldEmail = null, oldCompany = null, state = null;

        while(index < oldValues.size()) {
            oldName = oldValues.get(0).getSupplierName();
            oldContact = oldValues.get(0).getSupplierContactNo();
            oldEmail = oldValues.get(0).getSupplierEmail();
            oldCompany = oldValues.get(0).getCompanyName();
            index++;
        }

        // SCENARIOS
        //NCECm
        assert oldName != null;
        if(!oldName.equalsIgnoreCase(name) && !oldContact.equalsIgnoreCase(contact) && !oldEmail.equalsIgnoreCase(email) && !oldCompany.equalsIgnoreCase(company)) {
            query += "supplier_name = ?, contact = ?, email = ?, company_name = ? WHERE supplier_id = ?";
            state = "1";
        }
        //NECm
        else if(!oldName.equalsIgnoreCase(name) && !oldEmail.equalsIgnoreCase(email) && !oldCompany.equalsIgnoreCase(company)) {
            query += "supplier_name = ?, email = ?, company_name = ? WHERE supplier_id = ?";
            state = "2";
        }
        //NCE
        else if(!oldName.equalsIgnoreCase(name) && !oldContact.equalsIgnoreCase(contact) && !oldEmail.equalsIgnoreCase(email)) {
            query += "supplier_name = ?, contact = ?, email = ? WHERE supplier_id = ?";
            state = "3";
        }
        //NCCm
        else if(!oldName.equalsIgnoreCase(name) && !oldContact.equalsIgnoreCase(contact) && !oldCompany.equalsIgnoreCase(company)) {
            query += "supplier_name = ?, contact = ?, company_name = ? WHERE supplier_id = ?";
            state = "4";
        }
        //CECm
        else if(!oldContact.equalsIgnoreCase(contact) && !oldEmail.equalsIgnoreCase(email) && !oldCompany.equalsIgnoreCase(company)) {
            query += "contact = ?, email = ?, company_name = ? WHERE supplier_id = ?";
            state = "5";
        }
        //NC
        else if(!oldName.equalsIgnoreCase(name) && !oldContact.equalsIgnoreCase(contact)) {
            query += "supplier_name = ?, contact = ? WHERE supplier_id = ?";
            state = "6";
        }
        //NE
        else if(!oldName.equalsIgnoreCase(name) && !oldEmail.equalsIgnoreCase(email) ) {
            query += "supplier_name = ?, email = ? WHERE supplier_id = ?";
            state = "7";
        }
        //NCm
        else if(!oldName.equalsIgnoreCase(name) && !oldCompany.equalsIgnoreCase(company)) {
            query += "supplier_name = ?, company_name = ? WHERE supplier_id = ?";
            state = "8";
        }
        //CE
        else if(!oldContact.equalsIgnoreCase(contact) && !oldEmail.equalsIgnoreCase(email)) {
            query += "contact = ?, email = ? WHERE supplier_id = ?";
            state = "9";
        }
        //CCm
        else if(!oldContact.equalsIgnoreCase(contact) && !oldCompany.equalsIgnoreCase(company)) {
            query += "contact = ?, company_name = ? WHERE supplier_id = ?";
            state = "10";
        }
        //N
        else if(!oldName.equalsIgnoreCase(name)) {
            query += "supplier_name = ? WHERE supplier_id = ?";
            state = "11";
        }
        //C
        else if(!oldContact.equalsIgnoreCase(contact)) {
            query += "contact = ? WHERE supplier_id = ?";
            state = "12";
        }
        //E
        else if(!oldEmail.equalsIgnoreCase(email)) {
            query += "email = ? WHERE supplier_id = ?";
            state = "13";
        }
        //Cm
        else if(!oldCompany.equalsIgnoreCase(company)) {
            query += "company_name = ? WHERE supplier_id = ?";
            state = "14";
        }

        if(connection != null) {
            PreparedStatement ps = connection.prepareStatement(query);
            try {
                if (state != null) {
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

                if (output == 1) {
                    return 2; // update successful
                } else {
                    return 0; // update failed
                }
            } catch (SQLException e) {
                System.out.println("Error while running the query: " + e.getMessage());
                ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            } finally {
                db.closeConnection(connection, null, ps, null);
            }
        }

        return output;
    }

    private Integer deleteSupplier(String id) throws SQLException {
        Connection connection = db.establishConnection();
        String query = "DELETE FROM supplier WHERE supplier_id = '"+id+"'";
        int output = -1;

        if(connection != null) {
            try {
                statement = connection.createStatement();
                output = statement.executeUpdate(query);
                if (output == 1) {
                    return 4;
                } else {
                    return 5;
                }
            } catch (SQLException e) {
                System.out.println("Error while running the query: " + e.getMessage());
                ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            } finally {
                db.closeConnection(connection, statement, null, null);
            }
        }

        return output;
    }


    private ObservableList<Supplier> searchSupplier(String searchInput) throws SQLException {
        Connection connection = db.establishConnection();
        String query = "SELECT * FROM supplier WHERE supplier_name LIKE ? OR contact = ?";
        String wildcard = "%" + searchInput + "%";

        if(connection != null) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            try {
                preparedStatement.setString(1, wildcard);
                preparedStatement.setString(2, searchInput);

                resultSet = preparedStatement.executeQuery();
                data = convertData(resultSet);
            } catch (SQLException e) {
                System.out.println("Error while running the query: " + e.getMessage());
                ErrorMsgHelper.catchError("Error while running the query: " + e.getErrorCode());
            } finally {
                db.closeConnection(connection, null, preparedStatement, resultSet);
            }
        }

        return data;
    }

    private ObservableList<Supplier> convertData(ResultSet resultSet) throws SQLException {
        ObservableList<Supplier> supplierList = FXCollections.observableArrayList();

        try {
            while(resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(resultSet.getString("supplier_id"));
                supplier.setSupplierName(resultSet.getString("supplier_name"));
                supplier.setSupplierContactNo(resultSet.getString("contact"));
                supplier.setSupplierEmail(resultSet.getString("email"));
                supplier.setCompanyName(resultSet.getString("company_name"));

                supplierList.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return supplierList;
    }
}
