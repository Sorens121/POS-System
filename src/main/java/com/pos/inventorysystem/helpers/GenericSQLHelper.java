package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.db.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenericSQLHelper {

    public static boolean checkDuplicate(String selectQuery, String valueToCheck) throws SQLException, ClassNotFoundException {
        try(PreparedStatement preparedStatement = db.myConnection().prepareStatement(selectQuery)) {
            preparedStatement.setString(1, valueToCheck);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            } else {
                return false;
            }
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("Error while running the query");
        }
        return false;
    }
}
