package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.database.ColumnIndex;
import com.meritatech.myrewardzpos.database.NotNull;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.database.Unique;
import com.meritatech.myrewardzpos.enums.SalesOrderStatus;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@PersistAnnotation
public class SalesInvoiceRecord extends DatabaseObject {


    @ColumnIndex
    @SerializedName("recId")
    public long recId;
    public long getrecId(){
        return  recId;
    }


    @SerializedName("salesOrderId")
    public String salesOrderId;

    public String getfsalesOrderId() {
        return salesOrderId;
    }


    @SerializedName("salesOrderStatus")
    public SalesOrderStatus salesOrderStatus;

    public SalesOrderStatus getsalesOrderStatus() {
        return salesOrderStatus;
    }


    @SerializedName("customerId")
    public String customerId;

    public String getcustomerId() {
        return customerId;
    }

    @SerializedName("invoiceCancelAgeDateTime")
    public Date invoiceCancelAgeDateTime;

    public Date getinvoiceCancelAgeDateTime() {
        return invoiceCancelAgeDateTime;
    }

    @SerializedName("invoiceType")
    public String invoiceType;

    public String getinvoiceType() {
        return invoiceType;
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


    @SerializedName("customerType")
    public String customerType;

    public String getcustomerType() {
        return customerType;
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

    @SerializedName("isPosted")
    public Boolean isPosted;

    public Boolean getisPosted() {
        return isPosted;
    }

    @SerializedName("lastPoints")
    public int lastPoints;

    public int getlastPoints() {
        return lastPoints;
    }

    @SerializedName("newPoints")
    public int newPoints;

    public int getnewPointss() {
        return newPoints;
    }

    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}
