package com.automated.teller.machine;

import java.sql.*;

public class ConnectionDB {
    protected Statement createConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ebank?serverTimezone=UTC","root","root");
            return connection.createStatement();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
