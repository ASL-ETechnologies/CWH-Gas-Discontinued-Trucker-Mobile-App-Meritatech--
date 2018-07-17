package com.meritatech.myrewardzpos.global;

import com.meritatech.myrewardzpos.controller.Customer;
import com.meritatech.myrewardzpos.controller.Inventory;

import java.util.ArrayList;

/**
 * Created by user on 12/6/2017.
 */

public class ReceiptVariableData {
    private ReceiptVariableData(){}
    public static ArrayList<Inventory> inventoryVariable;
    public static Customer customerVariable;
}
