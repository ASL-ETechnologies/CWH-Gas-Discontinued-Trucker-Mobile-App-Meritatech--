package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;

public class InvoiceData{
    @SerializedName("recCnt")
    public String recCnt;
    public String getrecCnt(){
        return  recCnt;
    }

    @SerializedName("mdTrxns")
    public String mdTrxns;
    public String getmdTrxns(){
        return  recCnt;
    }
}
