package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.*;

import java.util.ArrayList;

/**
 * Created by user on 12/22/2017.
 */

public class SalesOrderPost {

    @SerializedName("recID")
    public long recID;
    public long getrecID(){
        return  recID;
    }

    @SerializedName("customerId")
    public String customerId;
    public String getcustomerId(){
        return  customerId;
    }

    @SerializedName("invoiceType")
    public String invoiceType;
    public String getinvoiceType(){
        return  invoiceType;
    }

    @SerializedName("salesOrderId")
    public String salesOrderId;
    public String getorderId(){
        return  salesOrderId;
    }

    @SerializedName("salesmanId")
    public String salesmanId;
    public String getsalesmanId(){
        return  salesmanId;
    }

    @SerializedName("invoiceDateTime")
    public String invoiceDateTime;
    public String getinvoiceDateTime(){
        return  invoiceDateTime;
    }

    @SerializedName("invoiceNumber")
    public String invoiceNumber;
    public String getinvoiceNumber(){
        return  invoiceNumber;
    }

    @SerializedName("parentStoreId")
    public String parentStoreId;
    public String getparentStoreId(){
        return  parentStoreId;
    }

    @SerializedName("storeId")
    public String storeId;
    public String getstoreId(){
        return  storeId;
    }

    @SerializedName("fName")
    public String fName;
    public String getfName(){
        return  fName;
    }

    @SerializedName("lName")
    public String lName;
    public String getlName(){
        return  lName;
    }

    @SerializedName("phone1")
    public String phone1;
    public String getphone1(){
        return  phone1;
    }

    @SerializedName("phone2")
    public String phone2;
    public String getphone2(){
        return  phone2;
    }

    @SerializedName("email")
    public String email;
    public String getemail(){
        return  email;
    }

    @SerializedName("longi")
    public String longi;
    public String getlongitude(){
        return  longi;
    }

    @SerializedName("customerType")
    public String customerType;
    public String getcustomerType(){
        return  customerType;
    }

    @SerializedName("lati")
    public String lati;
    public String getlatitude(){
        return  lati;
    }

    @SerializedName("comment")
    public String comment;
    public String getcomment(){
        return  comment;
    }

    @SerializedName("status")
    public String status;
    public String getstatus(){
        return  status;
    }

    @SerializedName("slsDetailArr")
    public ArrayList<InventotyPost> slsDetailArr;
    public ArrayList<InventotyPost>getSalesDetails(){
        return slsDetailArr;
    }





}
