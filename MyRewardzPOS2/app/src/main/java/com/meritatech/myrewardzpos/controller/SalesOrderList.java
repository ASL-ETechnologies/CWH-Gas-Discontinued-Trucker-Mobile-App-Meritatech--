package com.meritatech.myrewardzpos.controller;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;

import java.util.List;

/**
 * Created by user on 11/27/2017.
 */

public class SalesOrderList {
    public List<SalesOrderRecord> items;
    public List<SalesOrderRecord> getitems()
    {
        return items;
    }

}
