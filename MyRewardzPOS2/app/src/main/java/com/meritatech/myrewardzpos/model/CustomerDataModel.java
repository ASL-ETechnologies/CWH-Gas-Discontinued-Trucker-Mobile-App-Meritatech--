package com.meritatech.myrewardzpos.model;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.Customer;
import com.meritatech.myrewardzpos.dataObj.CustomerDataObj;

import java.util.ArrayList;

public class CustomerDataModel {
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

    @SerializedName("dataObj")
    public CustomerDataObj dataObj;
    public CustomerDataObj getdataObj(){
        return dataObj;
    }

}
