package com.pos.inventorysystem.actions;

import com.pos.inventorysystem.Model.Employee;
import com.pos.inventorysystem.db.db;
import com.pos.inventorysystem.helpers.ErrorMsgHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class EmployeeActions {
    private Statement statement;
    private ResultSet resultSet = null;
    private String query = null;

    private ObservableList<Employee> data = null;

    public ObservableList<Employee> getAllRecords() throws SQLException {
        try {
            return getAllEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<Employee> getOneRecord(String id) throws SQLException {
        try {
            return getOneEmployee(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer createNewRecord(String employeeId, String name, String contact, String email) throws SQLException {
        try {
            return createNewEmployee(employeeId, name, contact, email);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer updateRecord(String employeeId, String name, String contact, String email) throws SQLException {
        try {
            return updateEmployee(employeeId, name, contact, email);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Integer deleteRecord(String id) throws SQLException {
        try {
            return deleteEmployee(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<Employee> searchRecord(String searchInput) throws SQLException {
        try {
            return searchEmployee(searchInput);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private ObservableList<Employee> getAllEmployees() throws SQLException {
        Connection connection = db.establishConnection();
        query = "SELECT * FROM employee";

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

    private ObservableList<Employee> getOneEmployee(String employeeId) throws SQLException {
        Connection connection = db.establishConnection();
        query = "SELECT * FROM employee WHERE employee_id = '"+employeeId+"'";

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

    private Integer createNewEmployee(String employeeId, String name, String contact, String email) throws SQLException {
        Connection connection = db.establishConnection();
        query = "INSERT INTO employee (employee_id, employee_name, contact_no, email) VALUES ('"+employeeId+"', '"+name+"', '"+contact+"', '"+email+"')";
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

    private Integer updateEmployee(String employeeId, String name, String contact, String email) throws SQLException {
        Connection connection = db.establishConnection();
        String updateQuery = "UPDATE employee SET ";
        int output = -1, index = 0;

        ObservableList<Employee> oldValues = getOneEmployee(employeeId);

        String oldName = null, oldContact = null, oldEmail = null, state = null;

        while(index < oldValues.size()) {
            oldName = oldValues.get(0).getEmployeeName();
            oldContact = oldValues.get(0).getContactNo();
            oldEmail = oldValues.get(0).getEmail();
            index++;
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

        if(connection != null) {
            PreparedStatement ps = connection.prepareStatement(updateQuery);
            try {
                if (state != null) {
                    switch (state) {
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

                if (output == 1) {
                    return 2; // update message
                } else {
                    return 0; // failed update message
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

    private Integer deleteEmployee(String employeeId) throws SQLException {
        Connection connection = db.establishConnection();
        query = "DELETE FROM employee WHERE employee_id = '"+employeeId+"'";
        int output = -1;

        if(connection != null) {
            try {
                statement = connection.createStatement();
                output = statement.executeUpdate(query);

                if (output == 1) {
                    return 4; // delete success
                } else {
                    return 5; // failed delete
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

    private ObservableList<Employee> searchEmployee(String searchInput) throws SQLException {
        Connection connection = db.establishConnection();
        query = "SELECT * FROM employee WHERE employee_name LIKE ? OR email LIKE ? OR contact_no LIKE ? OR employee_id = ?";
        String wildcard = "%" + searchInput + "%";

        if (connection != null) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            try {
                preparedStatement.setString(1, wildcard);
                preparedStatement.setString(2, wildcard);
                preparedStatement.setString(3, wildcard);
                preparedStatement.setString(4, searchInput);

                resultSet = preparedStatement.executeQuery();
                data = convertData(resultSet);
            } catch (SQLException e) {
                System.out.println("Error while running the query");
                e.printStackTrace();
            } finally {
                db.closeConnection(connection, null, preparedStatement, resultSet);
            }
        }
        return data;
    }

    private static ObservableList<Employee> convertData(ResultSet resultSet) throws SQLException {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        try {
            while(resultSet.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(resultSet.getString("employee_id"));
                employee.setEmployeeName(resultSet.getString("employee_name"));
                employee.setContactNo(resultSet.getString("contact_no"));
                employee.setEmail(resultSet.getString("email"));

                employeeList.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return employeeList;
    }
}
