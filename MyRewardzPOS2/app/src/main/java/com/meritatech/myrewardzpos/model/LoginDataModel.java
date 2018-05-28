package com.meritatech.myrewardzpos.model;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.Customer;
import com.meritatech.myrewardzpos.controller.LoginResponse;
import com.meritatech.myrewardzpos.dataObj.LoginDataObj;

import java.util.ArrayList;

/**
 * Created by user on 12/8/2017.
 */

public class LoginDataModel {
    @SerializedName("statusCode")
    public String StatusCode;
    public String getStatusCode(){
        return  StatusCode;
    }
    @SerializedName("errorMessage")
    public String ErrorMessage;
    public String getErrorMessage(){
        return ErrorMessage;
    }

    @SerializedName("dataObj")
   public LoginDataObj dataObj;
    public LoginDataObj getdataObj(){
        return dataObj;
    }

}
