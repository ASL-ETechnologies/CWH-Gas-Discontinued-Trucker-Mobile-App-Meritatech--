package com.meritatech.myrewardzpos.global;

import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;

import java.util.ArrayList;

/**
 * Created by user on 11/30/2017.
 */

public class SalesOrderVariableData {
    private SalesOrderVariableData(){}
    public static ArrayList<Inventory> salesOrderVariable;
    public static double Totals;
    public static double getTotals(){
        return Totals;
    }
    public static boolean IsSalesOrder;
}


