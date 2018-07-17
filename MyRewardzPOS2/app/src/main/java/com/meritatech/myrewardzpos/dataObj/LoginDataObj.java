package com.meritatech.myrewardzpos.dataObj;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.LoginResponse;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;

import java.util.ArrayList;

/**
 * Created by user on 12/20/2017.
 */

public class LoginDataObj {
    @SerializedName("recCnt")
    public int recCount;
    public int getrecCount(){
        return  recCount;
    }
    @SerializedName("totRecMatch")
    public int totRecMatch;
    public int gettotRecMatch(){
        return  totRecMatch;
    }

    @SerializedName("Data")
       public ArrayList<LoginResponse> Data;
    public ArrayList<LoginResponse>getData(){
        return Data;
    }
}
