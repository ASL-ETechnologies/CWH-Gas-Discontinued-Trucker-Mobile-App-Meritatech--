package com.meritatech.myrewardzpos.model;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.dataObj.CustomerAvailabilityDataObj;
import com.meritatech.myrewardzpos.dataObj.CustomerUsabilityDataObj;

public class CustomerUsabilityDataModel {
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
    public CustomerUsabilityDataObj dataObj;
    public CustomerUsabilityDataObj getData(){
        return dataObj;
    }

}
