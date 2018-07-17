package com.meritatech.myrewardzpos.controller;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.database.ColumnIndex;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.ArrayList;
import java.util.UUID;

@PersistAnnotation
public class SalesOrder extends DatabaseObject {
    @ColumnIndex
    @SerializedName("salesOrderId")
    public String salesOrderId;
    public String getfsalesOrderId(){
        return  salesOrderId;
    }
    @SerializedName("orderDate")
    public String orderDate;
    public String getparorderDate(){
        return orderDate;
    }
    @SerializedName("customerId")
    public String customerId;
    public String getcustomerId(){
        return customerId;
    }
    @SerializedName("fName")
    public String fName;
    public String getfName(){
        return fName;
    }
    @SerializedName("lName")
    public String lName;
    public String getlName(){
        return lName;
    }
    @SerializedName("address")
    public String address;
    public String getaddress(){
        return address;
    }
    @SerializedName("lati")
    public String lati;
    public String getlati(){
        return lati;
    }
    @SerializedName("longi")
    public String longi;
    public String getlongi(){
        return longi;
    }
    @SerializedName("comment")
    public String comment;
    public String getcomment(){
        return comment;
    }
    @SerializedName("phone")
    public String phone;
    public String getphone(){
        return phone;
    }
    @SerializedName("status")
    public String status;
    public String getstatus(){
        return status;
    }

    @SerializedName("soDetailArr")
    public ArrayList<Inventory> SalesDetails;
    public ArrayList<Inventory>getSalesDetails(){
        return SalesDetails;
    }

    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}