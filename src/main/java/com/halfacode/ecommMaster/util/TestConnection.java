package com.halfacode.ecommMaster.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    private static final String URL = "jdbc:mysql://146.190.72.200:3306/ecommerce-sh";
    private static final String USER = "root";
    private static final String PASSWORD = "0912658511";

    public static void main(String[] args) {
        try {
            System.out.println("Loading MySQL driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Attempting connection...");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            if (conn != null) {
                System.out.println("Connection successful 🎉");
                conn.close();
            } else {
                System.out.println("Connection failed 😭");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found 🧨");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred 💥");
            e.printStackTrace();
        }
    }
}
