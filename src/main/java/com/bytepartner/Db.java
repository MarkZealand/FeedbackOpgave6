package com.bytepartner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {

    private static final String URL  = "jdbc:mysql://localhost:3306/characters";
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "MySQL JDBC driver ikke fundet: " + e.getMessage());
        }
    }

    public Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}