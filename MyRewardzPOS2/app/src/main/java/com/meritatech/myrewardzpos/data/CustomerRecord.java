package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.database.ColumnIndex;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.database.Unique;
import com.meritatech.myrewardzpos.model.DatabaseObject;


import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Waithera on 12/1/2017.
 */
@PersistAnnotation
public class CustomerRecord extends DatabaseObject {
    public CustomerRecord() {

    }

    public CustomerRecord(String lname) {
        this.lName = lname;
    }

    public String getName() {
        return fName;
    }

    @Unique
    @ColumnIndex
    @SerializedName("customerId")
    public String customerId;

    public String getcustomerId() {
        return customerId;
    }

    @ColumnIndex
    @SerializedName("fName")
    public String fName;

    public String getfName() {
        return fName;
    }

    @ColumnIndex
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

    @SerializedName("points")
    public String points;

    public String getpoints() {
        return points;
    }

    @SerializedName("newpoints")
    public int newpoints;

    public int getnewpoints() {
        return newpoints;
    }

    @SerializedName("lastpoints")
    public int lastpoints;

    public int getlastpoints() {
        return lastpoints;
    }

    @ColumnIndex
    @SerializedName("phone1")
    public String phone1;

    public String getphone1() {
        return phone1;
    }

    @ColumnIndex
    @SerializedName("phone2")
    public String phone2;

    public String getphone2() {
        return phone2;
    }

    @ColumnIndex
    @SerializedName("email")
    public String email;

    public String getemail() {
        return email;
    }

    @SerializedName("longi")
    public String longi;

    public String getlongi() {
        return longi;
    }

    @SerializedName("lati")
    public String lati;

    public String getlati() {
        return lati;
    }

    @SerializedName("tStamp")
    @ColumnIndex
    public String tStamp;

    public String gettStamp() {
        return tStamp;
    }

    @SerializedName("salesmanId")
    public String salesmanId;

    public String getsalesmanId() {
        return salesmanId;
    }

    @ColumnIndex
    @Unique
    @SerializedName("orderId")
    public String orderId;

    public String getorderId() {
        return orderId;
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


    public void setcustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getcustomerId(String customerId) {
        return this.customerId = customerId;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setaddress(String address) {
        this.address = address;
    }

    public void setpoints(String points) {
        this.points = points;
    }

    public void setphone1(String phone1) {
        this.phone1 = phone1;
    }

    public void setphone2(String phone2) {
        this.phone2 = phone2;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public void setlongi(String longi) {
        this.longi = longi;
    }

    public void setlati(String lati) {
        this.lati = lati;
    }

    public void settStamp(String tStamp) {
        this.tStamp = tStamp;
    }

    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}
