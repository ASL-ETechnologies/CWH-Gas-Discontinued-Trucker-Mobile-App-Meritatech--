package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.controller.OrderItem;
import com.meritatech.myrewardzpos.database.ColumnIndex;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.database.Unique;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.ArrayList;
import java.util.UUID;

@PersistAnnotation
public class SalesOrderRecord extends DatabaseObject {


    @ColumnIndex
    @SerializedName("salesOrderId")
    public String salesOrderId;

    public String getfsalesOrderId() {
        return salesOrderId;
    }

    @SerializedName("customerId")
    public String customerId;

    public String getcustomerId() {
        return customerId;
    }


    @SerializedName("invoiceDateTime")
    public String invoiceDateTime;

    public String getinvoiceDateTime() {
        return invoiceDateTime;
    }

    @SerializedName("salesmanId")
    public String salesmanId;

    public String getsalesmanId() {
        return salesmanId;
    }



    @Unique
    @SerializedName("invoiceNumber")
    public String invoiceNumber;

    public String getinvoiceNumber() {
        return invoiceNumber;
    }

    @SerializedName("parentStoreId")
    public String parentStoreId;

    public String getparentStoreId() {
        return parentStoreId;
    }

    @SerializedName("storeId")
    public String storeId;

    public String getstoreId() {
        return storeId;
    }

    @SerializedName("phone1")
    public String phone1;

    public String getphone1() {
        return phone1;
    }

    @SerializedName("phone2")
    public String phone2;

    public String getphone2() {
        return phone2;
    }

    @SerializedName("email")
    public String email;

    public String getemail() {
        return email;
    }

    @SerializedName("longitude")
    public String longitude;

    public String getlongitude() {
        return longitude;
    }

    @SerializedName("latitude")
    public String latitude;

    public String getlatitude() {
        return latitude;
    }


    @SerializedName("sent")
    public int sent;

    public int getsent() {
        return sent;
    }


    @SerializedName("orderDate")
    public String orderDate;

    public String getparorderDate() {
        return orderDate;
    }

    @SerializedName("fName")
    public String fName;

    public String getfName() {
        return fName;
    }

    @SerializedName("lName")
    public String lName;

    public String getlName() {
        return lName;
    }

    @SerializedName("address")
    public String address;

    public String getaddress() {
        return address;
    }

    @SerializedName("lati")
    public String lati;

    public String getlati() {
        return lati;
    }

    @SerializedName("longi")
    public String longi;

    public String getlongi() {
        return longi;
    }

    @SerializedName("comment")
    public String comment;

    public String getcomment() {
        return comment;
    }

    @SerializedName("phone")
    public String phone;

    public String getphone() {
        return phone;
    }

    @SerializedName("status")
    public String status;

    public String getstatus() {
        return status;
    }

    @SerializedName("soDetailArr")
    public ArrayList<Inventory> SalesDetails;

    public ArrayList<Inventory> getSalesDetails() {
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
