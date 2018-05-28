package com.meritatech.myrewardzpos.data;

import com.meritatech.myrewardzpos.database.SugarRecord;

import java.util.List;

/**
 * Created by Waithera on 12/17/2017.
 */

public class BulkOperations {
boolean isSuccess=false;
    public boolean DeleteAllSales()
    {

        SalesOrderRecord.deleteAll(SalesOrderRecord.class);

        return isSuccess;
    }


    public boolean DeleteAllCustomers()
    {

        CustomerRecord.deleteAll(CustomerRecord.class);

        return isSuccess;
    }

    public boolean DeleteAllSInventory()
    {

        InventoryRecord.deleteAll(InventoryRecord.class);

        return isSuccess;
    }

}
