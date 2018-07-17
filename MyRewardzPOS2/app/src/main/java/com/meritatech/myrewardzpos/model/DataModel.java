package com.meritatech.myrewardzpos.model;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.Inventory;

import java.util.ArrayList;

public class DataModel {
    @SerializedName("statusCode")
    public String StatusCode;
    public String getStatusCode(){
        return  StatusCode;
    }
    @SerializedName("ErrorMessage")
    public String ErrorMessage;
    public String getErrorMessage(){
        return ErrorMessage;
    }

    @SerializedName("Data")
    public ArrayList<Inventory> Data;
    public ArrayList<Inventory>getData(){
        return Data;
    }

}

