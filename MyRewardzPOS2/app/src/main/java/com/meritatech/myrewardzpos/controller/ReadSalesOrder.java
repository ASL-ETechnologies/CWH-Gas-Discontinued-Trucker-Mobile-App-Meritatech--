/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meritatech.myrewardzpos.controller;

import com.meritatech.myrewardzpos.controller.Functions;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.controller.SqliteDb;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Waithera
 */
public class ReadSalesOrder {

    Functions fn = new Functions();
    GlobalVariables globalVars = new GlobalVariables();
    SqliteDb db = new SqliteDb();
    // request for sales order read  API

    public String ReadSales(String salesManId) throws SQLException, JSONException {

        String route = "salesOrders/read.php?";
        // String salesManId = "1";
        String response = "";
        String routeUrl = "";
        boolean successOrders=false;

        routeUrl = route + "smId=" + salesManId + "&token=" + globalVars.token;
        response = fn.getPlainTextResponse();

        //  response = response.replaceAll("[\\]","");
        //now break the json and save a copy locally
        JSONArray req = new JSONArray(response);
        //since we dont have a key pick the value at index 0
        JSONObject jsonObject = req.getJSONObject(0);

        JSONArray obj = null;// = req.getJSONArray("records");
        int salesOrderId = 0;
        String orderDate = "";
        String fName = "";
        String lName = "";
        String address = "";
        String comment = "";
        String phone = "";
        int status = 0;
        double lati = 0;
        double longi = 0;
        int classId = 0;
        int sku = 0;
        String description = "";
        int quantity = 0;
        //String email = "";

        int customerId = jsonObject.getInt("customerId");
        salesOrderId = jsonObject.getInt("salesOrderId");
        orderDate = jsonObject.getString("orderDate");
        fName = jsonObject.getString("fName");
        lName = jsonObject.getString("lName");
        address = jsonObject.getString("address");
        comment = jsonObject.getString("comment");
        phone = jsonObject.getString("phone");
        status = jsonObject.getInt("status");
        lati = jsonObject.getInt("status");
        longi = jsonObject.getInt("status");

        //make sure we have records
        //  if (jsonObject.has("OrderItem")) {
        obj = jsonObject.getJSONArray("OrderItem");
        SqliteDb.createSalesOrderTable();
        successOrders = db.insertSalesOrder(classId, orderDate, customerId, fName, lName, comment, phone, classId, sku, status);
  if (successOrders) {
        db.createSalesOrderDetailsTable();
        for (int i = 0; i < obj.length(); ++i) {
            JSONObject rec = obj.getJSONObject(i);
            // int customerId = rec.getInt("customerId");
            classId = rec.getInt("classId");
            sku = rec.getInt("sku");
            description = rec.getString("description");
            quantity = rec.getInt("quantity");
//            if (rec.NULL.equals(rec.get("lati")) || rec.NULL.equals(rec.get("longi"))) {
//                //||rec.NULL.equals(rec.get("phone2"))||rec.NULL.equals(rec.get("phone1"))||rec.NULL.equals(rec.get("email"))){
//
//                // phone1 = "";// rec.getString("phone1");
//                // phone2 = "";//rec.getString("phone2");
//                //  email = "";//rec.getString("email");
//                longi = 0;  //rec.getDouble("longi");
//                lati = 0;//rec.getDouble("lati");
//            } else {
//
//                longi = rec.getDouble("longi");
//                lati = rec.getDouble("lati");
//            }
//            if (rec.NULL.equals(rec.get("phone"))) {
//                //||rec.NULL.equals(rec.get("phone2"))||rec.NULL.equals(rec.get("phone1"))||rec.NULL.equals(rec.get("email"))){
//
//                phone = "";// rec.getString("phone1");
//                // phone2 = "";//rec.getString("phone2");
//                //email = "";//rec.getString("email");
//                //longi =0;  //rec.getDouble("longi");
//                //lati = 0;//rec.getDouble("lati");
//            } else {
//
//                phone = rec.getString("phone");
//
//            }

            //successOrders = db.insertSalesOrder(classId, orderDate, customerId, fName, lName, comment, phone, classId, sku, status);

       db.insertSalesOrderDetails(salesOrderId, classId, sku, description, quantity);

            ///call the dbsave func
        }
        
             
                
             
              

            }
        // }

        db.selectSalesOrder();

        return response;
    }

}
