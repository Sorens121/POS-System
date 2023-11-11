package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.db.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenericSQLHelper {

    public static boolean checkDuplicate(String selectQuery, String valueToCheck) throws SQLException {
        Connection connection = db.establishConnection();
        ResultSet resultSet = null;

        if(connection != null) {
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            try {
                preparedStatement.setString(1, valueToCheck);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return false;
                } else {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error while running the query");
            } finally {
                db.closeConnection(connection, null, preparedStatement, resultSet);
            }
        }
        return true;
    }

    public static boolean shouldUpdateRow(String selectQuery, String id, List<String> params) throws SQLException {
        Connection connection = db.establishConnection();
        ResultSet resultSet = null;

        if(connection != null) {
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            try {
                preparedStatement.setString(1, id);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    //Iterate through the params and compare with the ResultSet values
                    for (int i = 1; i <= params.size(); i++) {
                        String currentValue = resultSet.getString(i + 1);
                        String newValue = params.get(i - 1);
                        System.out.println("current values :" + currentValue);
                        System.out.println("new values :" + newValue);
                        //compare current value with the params value
                        if (!newValue.equals(currentValue)) {
                            return true; // change detected hence update is required
                        }
                    }
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error while running the query");
            } finally {
                db.closeConnection(connection, null, preparedStatement, resultSet);
            }
        }

        return false;
    }
}
