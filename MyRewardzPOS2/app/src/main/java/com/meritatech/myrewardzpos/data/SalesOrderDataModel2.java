package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.SalesOrder;

import java.util.ArrayList;

/**
 * Created by Waithera on 12/13/2017.
 */

public class SalesOrderDataModel2 {

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

    @SerializedName("Data")
    public ArrayList<SalesRecords> Data;
    public ArrayList<SalesRecords>getData(){
        return Data;
    }

}
