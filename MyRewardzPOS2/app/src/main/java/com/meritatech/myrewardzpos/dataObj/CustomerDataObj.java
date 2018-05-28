package com.meritatech.myrewardzpos.dataObj;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.Customer;
import com.meritatech.myrewardzpos.controller.SalesOrder;
import com.meritatech.myrewardzpos.data.CustomerRecord;

import java.util.ArrayList;

/**
 * Created by user on 12/19/2017.
 */

public class CustomerDataObj {
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
    public ArrayList<CustomerRecord> Data;
    public ArrayList<CustomerRecord> getData(){
        return Data;
    }
}
