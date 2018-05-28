package com.meritatech.myrewardzpos.controller;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderItem
{

    @SerializedName("classId")
    public String classId;
    public String getclassId(){
        return  classId;
    }
    @SerializedName("sku")
    public String sku;
    public String getsku(){
        return sku;
    }
    @SerializedName("description")
    public String description;
    public String getdescription(){
        return description;
    }
    @SerializedName("quantity")
    public String qty;
    public String getqty(){
        return qty;
    }
    @SerializedName("points")
    public String points;
    public String getpoints(){
        return points;
    }

    @SerializedName("itemNum")
    public String itemNum;
    public String getitemNum(){
        return itemNum;
    }
    @SerializedName("priceSold")
    public String priceSold;
    public String getpriceSold(){
        return priceSold;
    }

}