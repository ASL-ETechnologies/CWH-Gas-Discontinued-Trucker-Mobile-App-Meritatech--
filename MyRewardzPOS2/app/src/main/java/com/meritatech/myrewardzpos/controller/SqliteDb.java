/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meritatech.myrewardzpos.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
//import org.hibernate.ejb.util.NamingHelper;

/**
 *
 * @author Waithera
 */
public class SqliteDb {

SqliteDbConnection sqliteConn = new SqliteDbConnection();
//GlobalVariables globalVars = new GlobalVariables();
 String url = "jdbc:sqlite:D:/sqlite/db/TestApiDb.db";


    //create inentory table
    public static void createNewTable() throws SQLException {
        // SQLite connection string
       // String url = "jdbc:sqlite:D:/sqlite/db/TestApiDb.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS inventory4 (\n"
                + "	classId integer,\n"
                + "	sku integer NOT NULL,\n"
                + "	description TEXT NOT NULL,\n"
                + "	sellingPrice NUM NOT NULL,\n"
                + "	 PRIMARY KEY (classId, sku)"
                + ");";

        try {
            Connection conn = DriverManager.getConnection(GlobalVariables.Dburl);
            Statement stmt = conn.createStatement();

            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //create customer table
    public static void createCustomerTable() throws SQLException {
        // SQLite connection string
       

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS customer12(\n"
                + "	customerId integer NOT NULL,\n"
                + "	fName TEXT  ,\n"
                + "	lName TEXT  ,\n"
                + "	address TEXT  ,\n"
                + "	points integer  ,\n"
                + "	phone1 TEXT  ,\n"
                + "	phone2 TEXT  ,\n"
                + "	email TEXT  ,\n"
                + "	longi double  ,\n"
                + "	lati double  ,\n"
                + "	 PRIMARY KEY (customerId)"
                + ");";

        try {
            Connection conn = DriverManager.getConnection(GlobalVariables.Dburl);
            Statement stmt = conn.createStatement();

            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //create sales order table
    public static void createSalesOrderTable() throws SQLException {
        // SQLite connection string
        //String url = "jdbc:sqlite:D:/sqlite/db/TestApiDb.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS salesOrder4 (\n"
                + "	orderId integer NOT NULL,\n"
                + "	orderDate TEXT  ,\n"
                + "	customerId integer  ,\n"
                + "	fName TEXT  ,\n"
                + "	lName TEXT  ,\n"
                + "	comment TEXT  ,\n"
                + "	phone TEXT  ,\n"
                + "	class integer  ,\n"
                + "	sku integer  ,\n"
                + "	status integer  ,\n"
                + "	 PRIMARY KEY (orderId)"
                + ");";

        try {
            Connection conn = DriverManager.getConnection(GlobalVariables.Dburl);
            Statement stmt = conn.createStatement();

            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    
    //create sales order table
    public static void createSalesOrderDetailsTable() throws SQLException {
        // SQLite connection string
        //String url = "jdbc:sqlite:D:/sqlite/db/TestApiDb.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS salesOrderDetails (\n"
                + "	ROWID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	SalesorderId integer  ,\n"
                + "	classId integer  ,\n"
                + "	sku integer  ,\n"
                + "	description TEXT  ,\n"
                + "	quantity TEXT  ,\n"
                + "	 FOREIGN KEY(SalesorderId) REFERENCES artist(orderId) "
                + ");";

        try {
            Connection conn = DriverManager.getConnection(GlobalVariables.Dburl);
            Statement stmt = conn.createStatement();

            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
     //insert sales orders details
        public void insertSalesOrderDetails(int SalesorderId, int classId, int sku, String description, int quantity) {
       // String sql = "INSERT INTO salesOrderDetails(salesOrderId, classId, sku, description, quantity) VALUES(?,?,?,?,?)";
       //lets use replace fuction to either update or insert new record
       String sql="REPLACE INTO salesOrderDetails(salesOrderId, classId, sku, description, quantity)VALUES (?,?,?,?,?)";

        try {
            Connection conn = sqliteConn.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
             pstmt.setInt(1, SalesorderId);
            pstmt.setInt(2, classId);
            pstmt.setInt(3, sku);
            pstmt.setString(4, description);
            pstmt.setInt(5, quantity);
         
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    
    //insert sales orders
        public boolean insertSalesOrder(int orderId, String orderDate, int customerId, String fName, String lName, String comment,
            String phone, int class1, int sku, int status) {
            
            
            boolean success=false;
        //String sql = "INSERT INTO salesOrder4(orderId, orderDate, customerId, fName, lName, comment, phone, class, sku, status) VALUES(?,?,?,?,?,?,?,?,?,?)";
        String sql = "REPLACE INTO salesOrder4(orderId, orderDate, customerId, fName, lName, comment, phone, class, sku, status) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try {
            Connection conn = sqliteConn.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            pstmt.setString(2, orderDate);
            pstmt.setInt(3, customerId);
            pstmt.setString(4, fName);
            pstmt.setString(5, lName);
            pstmt.setString(6, comment);
            pstmt.setString(7, phone);
            pstmt.setInt(8, class1);
            pstmt.setDouble(9, sku);
            pstmt.setDouble(10, status);
            
            pstmt.executeUpdate();
            success=true;
          
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return  success;
    }
    
    
    

    /**
     * select all rows in the inventory table table
     */
    public void selectAll() {
        String sql = "SELECT classId, sku, description, sellingPrice FROM inventory4";

        try {
            Connection conn = sqliteConn.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("classId") + "\t"
                        + rs.getString("sku") + "\t"
                        + rs.getString("description") + "\t"
                        + rs.getString("sellingPrice"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * select all rows in the customer table table
     */
    public void selectAllCustomers() {
        String sql = "SELECT customerId, fName, lName, address, points, phone1, phone2, email, longi, lati FROM customer12";

        try {
            Connection conn = sqliteConn.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("customerId") + "\t"
                        + rs.getString("fName") + "\t"
                        + rs.getString("lName") + "\t"
                        + rs.getString("address") + "\t"
                        + rs.getInt("points") + "\t"
                        + rs.getString("phone1") + "\t"
                        + rs.getString("phone2") + "\t"
                        + rs.getString("email") + "\t"
                        + rs.getDouble("longi") + "\t"
                        + rs.getDouble("lati"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    
    
    
    
    //select Sales order
    
     public void selectSalesOrder() {
        String sql = "SELECT orderId, orderDate, customerId, fName, lName, comment, phone, class, sku, status FROM salesOrder4";

        try {
            Connection conn = sqliteConn.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                String name =rs.getString("fName");
                System.out.println(rs.getInt("orderId") + "\t"
                        + rs.getInt("orderDate") + "\t"
                        + rs.getInt("customerId") + "\t"
                        + rs.getString("fName") + "\t"
                        + rs.getInt("lName") + "\t"
                        + rs.getString("phone") + "\t"
                        + rs.getInt("class") + "\t"
                        + rs.getInt("sku") + "\t"
                        + rs.getInt("status"));
                       // + rs.getDouble("lati"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    

    public void insert(int classId, double sku, String description, double sellingPrice) {
      //  String sql = "INSERT INTO inventory4(classId,sku,description,sellingPrice) VALUES(?,?,?,?)";
       String sql = "REPLACE INTO inventory4(classId,sku,description,sellingPrice) VALUES(?,?,?,?)";

        try {
            Connection conn = sqliteConn.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, classId);
            pstmt.setDouble(2, sku);
            pstmt.setString(3, description);
            pstmt.setDouble(4, sellingPrice);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertCustomer(int customerId, String fName, String lName, String address, int points, String phone1,
            String phone2, String email, double longi, double lati) {
       // String sql = "INSERT INTO customer12(customerId,fName,lName,address,points,phone1,phone2,email,longi,lati) VALUES(?,?,?,?,?,?,?,?,?,?)";
        String sql = "REPLACE INTO customer12(customerId,fName,lName,address,points,phone1,phone2,email,longi,lati) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try {
            Connection conn = sqliteConn.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            pstmt.setString(2, fName);
            pstmt.setString(3, lName);
            pstmt.setString(4, address);
            pstmt.setInt(5, points);
            pstmt.setString(6, phone1);
            pstmt.setString(7, phone2);
            pstmt.setString(8, email);
            pstmt.setDouble(9, longi);
            pstmt.setDouble(10, lati);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //update 
    public void update(int classId, String description, double sellingPrice) {
        String sql = "UPDATE inventory SET sellingPrice = ? , "
                + "description = ? "
                + "WHERE classId = ?";

        try {
            Connection conn = sqliteConn.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // set the corresponding param
            pstmt.setString(1, description);
            pstmt.setDouble(2, sellingPrice);
            pstmt.setInt(3, classId);
            // update 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

//    delete 
    public void delete(int classId) {
        String sql = "DELETE FROM inventory WHERE classId = ?";

        try {
            Connection conn = sqliteConn.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // set the corresponding param
            pstmt.setInt(1, classId);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
