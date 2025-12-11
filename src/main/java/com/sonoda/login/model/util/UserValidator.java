package com.sonoda.login.model.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserValidator {
    public static boolean isUserValid(String user){
        String sql = "SELECT username FROM users WHERE BINARY username = ?;";
        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,user);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()){
                return false;
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
