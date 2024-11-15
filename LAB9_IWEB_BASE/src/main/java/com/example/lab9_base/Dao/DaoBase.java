package com.example.lab9_base.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DaoBase {

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Se tiene conexion a una base de datos nombrada lab9. Asegúrese que su user y password sea root.
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/lab9", "root", "root");
    }
}