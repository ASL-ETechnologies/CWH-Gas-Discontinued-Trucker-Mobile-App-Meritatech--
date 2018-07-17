package com.meritatech.myrewardzpos.dataObj;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.data.LogRecord;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;

import java.util.ArrayList;

public class LogsDataObj {
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

    @SerializedName("logRecord")
    public ArrayList<LogRecord> logRecord;
    public ArrayList<LogRecord>getData(){
        return logRecord;
    }
}
