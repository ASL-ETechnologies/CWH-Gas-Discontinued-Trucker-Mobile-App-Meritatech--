package com.meritatech.myrewardzpos.dataObj;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.Customer;
import com.meritatech.myrewardzpos.data.InventoryRecord;

import java.util.ArrayList;

/**
 * Created by user on 12/20/2017.
 */

public class InventoryDataObj {
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
    public ArrayList<InventoryRecord> Data;
    public ArrayList<InventoryRecord>getData(){
        return Data;
    }
}
