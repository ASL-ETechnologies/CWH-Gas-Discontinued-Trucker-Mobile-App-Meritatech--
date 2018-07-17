package com.meritatech.myrewardzpos.model;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;

import java.util.ArrayList;

public class InvoiceDataModel {
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
    public InvoiceDataObj dataObj;
    public InvoiceDataObj getData(){
        return dataObj;
    }

}
