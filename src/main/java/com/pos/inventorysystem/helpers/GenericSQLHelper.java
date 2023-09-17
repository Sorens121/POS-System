package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.db.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenericSQLHelper {

    public static boolean checkDuplicate(String selectQuery, String valueToCheck) throws SQLException, ClassNotFoundException {
        try(PreparedStatement preparedStatement = db.myConnection().prepareStatement(selectQuery)) {
            preparedStatement.setString(1, valueToCheck);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return false;
            } else {
                return true;
            }
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("Error while running the query");
        }
        return true;
    }

    public static boolean shouldUpdateRow(String selectQuery, String id, List<String> params) {
        try(PreparedStatement preparedStatement = db.myConnection().prepareStatement(selectQuery)) {
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    //Iterate through the params and compare with the ResultSet values
                    for (int i = 1; i <= params.size(); i++) {
                        String currentValue = resultSet.getString(i+1);
                        String newValue = params.get(i-1);
                        System.out.println("current values :" + currentValue);
                        System.out.println("new values :" + newValue);
                        //compare current value with the params value
                        if (!newValue.equals(currentValue)) {
                            return true; // change detected hence update is required
                        }
                    }
                    return false;
                }

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error while running the query");
        }

        return false;
    }
}
