package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.Model.Customer;
import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.ErrorMsgHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CustomerActions {

    private Statement statement;
    private ResultSet resultSet = null;
    private String sql = null;
    private ObservableList<Customer> data = null;


    public ObservableList<Customer> getAllRecords() throws SQLException {
        try {
            return getAllCustomers();
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<Customer> getOneRecord(String id) throws SQLException {
        try {
            return getOneCustomer(id);
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer createNewRecord(String customerId, String name, String contact, String email) throws SQLException {
        try {
            return createCustomer(customerId, name, contact, email);
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer updateRecord(String customerId, String name, String contact, String email) throws SQLException {
        try {
            return updateCustomer(customerId, name, contact, email);
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer deleteRecord(String id) throws SQLException {
        try {
            return deleteCustomer(id);
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<Customer> searchRecord(String searchInput) throws SQLException {
        try {
            return searchCustomer(searchInput);
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private ObservableList<Customer> getAllCustomers() throws SQLException {
        Connection connection = db.establishConnection();
        sql = "SELECT * FROM customer";

        if(connection != null) {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                data = convertData(resultSet);
            } catch (SQLException e) {
                System.out.println("Error while running query");
                e.printStackTrace();
                throw e;
            } finally {
                db.closeConnection(connection, statement, null, resultSet);
            }
        }

        return data;
    }

    private ObservableList<Customer> getOneCustomer(String customerId) throws SQLException {
        Connection connection = db.establishConnection();
        sql = "SELECT * FROM customer WHERE customer_id = '"+customerId+"'";

        if(connection != null) {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);

                data = convertData(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, statement, null, resultSet);
            }
        }
        return data;
    }

    private Integer createCustomer(String customerId, String name, String contact, String email) throws SQLException {
        Connection connection = db.establishConnection();
        sql = "INSERT INTO customer (customer_id, customer_name, contact_no, email) VALUES ('"+customerId+"', '"+name+"', '"+contact+"', '"+email+"')";
        int output = -1;
        if(connection != null) {
            try {
                statement = connection.createStatement();
                output = statement.executeUpdate(sql);
                return output;
            } catch (SQLException e) {
                System.out.println("Error while running query");
                ErrorMsgHelper.catchError(String.valueOf(e.getErrorCode()));
            } finally {
                db.closeConnection(connection, statement, null, null);
            }

        }
        return output;
    }

    private Integer updateCustomer(String customerId, String name, String contact, String email) throws SQLException {
        Connection connection = db.establishConnection();
        String updateQuery = "UPDATE customer SET ";
        int output = -1, index = 0;

        ObservableList<Customer> oldValues = getOneCustomer(customerId);

        String oldName = null, oldEmail = null, state = null, oldContact = null;

        while (index < oldValues.size()){
            oldName = oldValues.get(0).getCustomerName();
            oldContact = oldValues.get(0).getContactNumber();
            oldEmail = oldValues.get(0).getEmail();
            index++;
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
            updateQuery += "customer_name = ? WHERE customer_id = ?";
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

        if(connection != null) {
            PreparedStatement ps = connection.prepareStatement(updateQuery);
            try {
                if (state != null) {
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
                            ps.setString(1, name);
                            ps.setString(2, customerId);
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

                if (output == 1) {
                    return 2; // update success
                } else {
                    return 0; // error message
                }
            } catch (SQLException e) {
                System.out.println("Error while running query");
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, null, ps, null);
            }

        }
        return output;
    }

    private Integer deleteCustomer(String customerId) throws SQLException {
        Connection connection = db.establishConnection();
        sql = "DELETE FROM customer WHERE customer_id = '"+customerId+"'";
        int output = -1;

        if(connection != null) {
            try {
                statement = connection.createStatement();
                output = statement.executeUpdate(sql);

                if (output == 1) {
                    return 4; // delete success
                } else {
                    return 5; // delete error
                }
            } catch (SQLException e) {
                System.out.println("Error while running query");
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, statement, null, null);
            }
        }
        return output;
    }

    private ObservableList<Customer> searchCustomer(String searchInput) throws SQLException {
        Connection connection = db.establishConnection();
        sql = "SELECT * FROM customer WHERE customer_name LIKE ? OR email LIKE ? OR contact_no = ?";
        String wildcard = "%" + searchInput + "%";

        if(connection != null) {
            PreparedStatement ps = connection.prepareStatement(sql);
            try {
                ps.setString(1, wildcard);
                ps.setString(2, wildcard);
                ps.setString(3, searchInput);

                resultSet = ps.executeQuery();
                data = convertData(resultSet);

            } catch (SQLException e) {
                System.out.println("Error while running query");
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, null, ps, resultSet);
            }
        }
        return data;
    }

    private static ObservableList<Customer> convertData(ResultSet resultSet) throws SQLException {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(resultSet.getString("customer_id"));
                customer.setCustomerName(resultSet.getString("customer_name"));
                customer.setContactNumber(resultSet.getString("contact_no"));
                customer.setEmail(resultSet.getString("email"));
                customerList.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return customerList;
    }
}
