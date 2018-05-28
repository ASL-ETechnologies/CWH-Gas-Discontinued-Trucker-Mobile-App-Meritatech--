package com.meritatech.myrewardzpos.model;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.dataObj.CustomerAvailabilityDataObj;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;

public class CustomerAvailabilityDataModel {
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
    public CustomerAvailabilityDataObj dataObj;
    public CustomerAvailabilityDataObj getData(){
        return dataObj;
    }

}
