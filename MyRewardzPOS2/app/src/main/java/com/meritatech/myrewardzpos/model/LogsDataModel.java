package com.meritatech.myrewardzpos.model;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;
import com.meritatech.myrewardzpos.dataObj.LogsDataObj;

public class LogsDataModel {
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
    public LogsDataObj dataObj;
    public LogsDataObj getData(){
        return dataObj;
    }

}
