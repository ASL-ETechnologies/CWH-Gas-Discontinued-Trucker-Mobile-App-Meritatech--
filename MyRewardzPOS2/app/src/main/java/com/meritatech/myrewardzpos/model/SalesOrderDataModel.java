package com.meritatech.myrewardzpos.model;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.SalesOrder;
import com.meritatech.myrewardzpos.dataObj.SalesOrderDataObj;

import java.util.ArrayList;

public class SalesOrderDataModel {
    @SerializedName("statusCode")
    public String StatusCode;
    public String getStatusCode(){
        return  StatusCode;
    }
    @SerializedName("Message")
    public String ErrorMessage;
    public String getErrorMessage(){
        return ErrorMessage;
    }

    @SerializedName("dataObj")
    public SalesOrderDataObj salesOrderDataObj;
    public SalesOrderDataObj getSalesOrderDataObj(){
        return salesOrderDataObj;
    }

}
