package com.meritatech.myrewardzpos.global;

import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;

import java.util.ArrayList;

/**
 * Created by user on 11/30/2017.
 */

public class InvoiceVariableData {
    private InvoiceVariableData(){}
    public static InvoiceDataObj invoiceDataObj;
    public  static InvoiceDataObj getInvoiceDataObj(){
        return invoiceDataObj;
    }
}


