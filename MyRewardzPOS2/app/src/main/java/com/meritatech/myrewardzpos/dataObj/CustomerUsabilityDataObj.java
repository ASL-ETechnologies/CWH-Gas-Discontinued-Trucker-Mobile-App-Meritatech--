package com.meritatech.myrewardzpos.dataObj;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.data.CustomerAvailabilityRecord;
import com.meritatech.myrewardzpos.data.CustomerUsabilityRecord;

import java.util.ArrayList;

/**
 * Created by user on 12/20/2017.
 */

public class CustomerUsabilityDataObj {
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
    public ArrayList<CustomerUsabilityRecord> Data;
    public ArrayList<CustomerUsabilityRecord>getData(){
        return Data;
    }
}
