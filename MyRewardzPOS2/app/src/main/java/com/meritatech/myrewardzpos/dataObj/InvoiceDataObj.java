package com.meritatech.myrewardzpos.dataObj;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.data.InventoryRecord;
import com.meritatech.myrewardzpos.data.SalesOrderPost;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;

import java.util.ArrayList;

/**
 * Created by user on 12/20/2017.
 */

public class InvoiceDataObj {

    @SerializedName("recCnt")
    public int recCnt;
    public int getrecCount(){
        return  recCnt;
    }

    @SerializedName("Data")
    public ArrayList<SalesOrderPost> Data;
    public ArrayList<SalesOrderPost> getData(){
        return Data;
    }
}
