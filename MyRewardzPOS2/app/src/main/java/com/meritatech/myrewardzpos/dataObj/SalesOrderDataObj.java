package com.meritatech.myrewardzpos.dataObj;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.SalesOrder;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;

import java.util.ArrayList;

/**
 * Created by user on 12/19/2017.
 */

public class SalesOrderDataObj {

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
    public ArrayList<SalesOrderRecord> Data;
    public ArrayList<SalesOrderRecord>getData(){
        return Data;
    }
}
