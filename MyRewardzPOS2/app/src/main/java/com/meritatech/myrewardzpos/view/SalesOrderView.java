package com.meritatech.myrewardzpos.view;

import com.meritatech.myrewardzpos.data.InventoryRecord;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;

import java.util.ArrayList;

/**
 * Created by Dennis Njagi on 1/26/2018.
 */

public interface SalesOrderView {
    void updateUi(ArrayList<SalesOrderRecord> customers);
}

