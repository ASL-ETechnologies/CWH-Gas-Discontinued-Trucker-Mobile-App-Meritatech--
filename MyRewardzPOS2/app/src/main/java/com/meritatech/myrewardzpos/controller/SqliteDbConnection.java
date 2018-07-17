/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meritatech.myrewardzpos.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Waithera
 */
public class SqliteDbConnection {
    
    
    
    
    // create the db 
        public static void createNewDatabase(String fileName) {

        //String url = "jdbc:sqlite:D:/sqlite/db/" + fileName;

        try {
            Connection conn = DriverManager.getConnection(GlobalVariables.Dburl);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                // System.out.println("The driver name is " + meta.getDriverName());
                // System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    
    
    Connection connect() {
        // SQLite connection string
        //String url = "jdbc:sqlite:D:/sqlite/db/TestApiDb.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(GlobalVariables.Dburl);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
}
