package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.Inventory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Waithera on 12/1/2017.
 */


public abstract class APIBase<T> {

    public int StatusCode;
    public String ErrorMessage;


    @SerializedName("statusCode")


    public int getstatusCode() {
        return StatusCode;
    }

    @SerializedName("Message")


    public String getMessage() {
        return ErrorMessage;
    }

    @SerializedName("dataObj")
    public T DataObj;

    public T getData(){ return DataObj;}
}
