package com.halfacode.ecommMaster.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/ecommerce", "root", "root");
            System.out.println("Connection successful!");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
