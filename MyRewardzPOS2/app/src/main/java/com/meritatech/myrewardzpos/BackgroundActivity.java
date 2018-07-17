package com.meritatech.myrewardzpos;

import com.meritatech.myrewardzpos.data.CustomerDataInfoRecord;
import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.data.CustomerRecordListAPI;
import com.meritatech.myrewardzpos.data.InventoryListAPI;
import com.meritatech.myrewardzpos.data.InventoryRecord;
import com.meritatech.myrewardzpos.data.InventotyPost;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.data.SalesInvoiceInventoryRecord;
import com.meritatech.myrewardzpos.data.SalesInvoiceRecord;
import com.meritatech.myrewardzpos.data.SalesOrderInventoryRecord;
import com.meritatech.myrewardzpos.data.SalesOrderPost;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;
import com.meritatech.myrewardzpos.data.SalesOrderRecordListAPI;
import com.meritatech.myrewardzpos.data.SalesTransactionsAPI;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;
import com.meritatech.myrewardzpos.utility.Utilities;

import java.util.ArrayList;

import static com.meritatech.myrewardzpos.MainActivity.appContext;

/**
 * Created by Dennis Mwangi on 4/26/2018.
 */

public class BackgroundActivity {


    public void pollSalesTransactions() {
        try {
            final MyPosBase myPosBase = new MyPosBase();

          /*  AsyncTask.execute(new Runnable() {
                @Override
                public void run() {*/
                    ArrayList<SalesInvoiceRecord> allsalestransactions = myPosBase.GetUnSentTransactions("0");
                    if (allsalestransactions != null && allsalestransactions.size() > 0) {
                        InventoryListAPI inventoryListAPI = new InventoryListAPI();

                        InvoiceDataObj invoiceDataObj = new InvoiceDataObj();
                        ArrayList<SalesOrderPost> salesOrderPostList = new ArrayList<SalesOrderPost>();
                        int recCount = 0;
                        for (int i = 0; i < allsalestransactions.size(); i++) {
                            ArrayList<SalesInvoiceInventoryRecord> invItems = inventoryListAPI.getInventoryByOrderId(allsalestransactions.get(i).getId().toString());
                            ArrayList<InventotyPost> invList = new ArrayList<InventotyPost>();
                            SalesOrderPost salesOrderPost1 = new SalesOrderPost();
                            salesOrderPost1.customerId = allsalestransactions.get(i).customerId;
                            salesOrderPost1.customerType = allsalestransactions.get(i).customerType;
                            salesOrderPost1.invoiceDateTime = allsalestransactions.get(i).invoiceDateTime;
                            salesOrderPost1.invoiceNumber = allsalestransactions.get(i).getId().toString();
                            salesOrderPost1.invoiceType = allsalestransactions.get(i).invoiceType;
                            salesOrderPost1.lati = allsalestransactions.get(i).lati;
                            salesOrderPost1.longi = allsalestransactions.get(i).longi;
                            salesOrderPost1.parentStoreId = allsalestransactions.get(i).parentStoreId;
                            salesOrderPost1.salesmanId = allsalestransactions.get(i).salesmanId;
                            salesOrderPost1.storeId = allsalestransactions.get(i).storeId;
                            salesOrderPost1.recID = allsalestransactions.get(i).getId();
                            salesOrderPost1.salesOrderId = allsalestransactions.get(i).salesOrderId;

                            if (allsalestransactions.get(i).lati == null) {
                                allsalestransactions.get(i).lati = "0";
                            }
                            if (allsalestransactions.get(i).longi == null) {
                                allsalestransactions.get(i).longi = "0";
                            }
                            salesOrderPost1.longi = allsalestransactions.get(i).longi;
                            salesOrderPost1.lati = allsalestransactions.get(i).lati;
                            salesOrderPost1.fName = allsalestransactions.get(i).fName;
                            salesOrderPost1.lName = allsalestransactions.get(i).lName;
                            salesOrderPost1.phone1 = allsalestransactions.get(i).phone1;
                            salesOrderPost1.phone2 = allsalestransactions.get(i).phone2;
                            salesOrderPost1.email = allsalestransactions.get(i).email;
                            recCount = invItems.size();
                            for (int j = 0; j < invItems.size(); j++) {
                                InventotyPost inventory = new InventotyPost();
                                inventory.itemNum = invItems.get(j).itemNumber;
                                inventory.points = invItems.get(j).points;
                                inventory.priceSold = invItems.get(j).priceSold;
                                inventory.quantity = invItems.get(j).qty;
                                if (invItems.get(j).taxPercentage == null) {
                                    invItems.get(j).taxPercentage = "0";
                                }
                                inventory.tax = invItems.get(j).taxPercentage;
                                invList.add(inventory);
                            }
                            salesOrderPost1.slsDetailArr = invList;
                            salesOrderPostList.add(salesOrderPost1);
                        }
                        invoiceDataObj.Data = salesOrderPostList;
                        invoiceDataObj.recCnt = salesOrderPostList.size();
                        SalesTransactionsAPI salesTransactionsAPI = new SalesTransactionsAPI();

                        salesTransactionsAPI.SalesTransactionWrite(invoiceDataObj);


                    }
               /* }
            });*/


        } catch (Exception ex) {
            Utilities.LogException(ex);
        }
    }

    public void syncInventory() {
        ArrayList<InventoryRecord> ir = InventoryRecord.findAllRecords(InventoryRecord.class);
        if (ir != null && ir.size() > 0) {
            InventoryRecord.deleteAll(InventoryRecord.class);
        }
        InventoryListAPI inventoryListAPI = new InventoryListAPI();
        inventoryListAPI.InventoryListAPICall();
    }

    public void syncSalesOrder() {
        ArrayList<SalesOrderRecord> allsalesorders = SalesOrderRecord.findAllRecords(SalesOrderRecord.class);
        if (allsalesorders != null && allsalesorders.size() > 0) {
            SalesOrderRecord.deleteAll(SalesOrderRecord.class);
            SalesOrderInventoryRecord.deleteAll(SalesOrderInventoryRecord.class);
        }
        SalesOrderRecordListAPI salesOrderRecordListAPI = new SalesOrderRecordListAPI();
        salesOrderRecordListAPI.SalesOrderRecordListAPICall();
    }

    public void syncContacts() {

        CustomerRecordListAPI customerRecordListAPI = new CustomerRecordListAPI(appContext);
        ArrayList<CustomerDataInfoRecord> cdi = CustomerDataInfoRecord.findAllRecords(CustomerDataInfoRecord.class);
        long custCount = CustomerRecord.count(CustomerRecord.class);

        if (cdi != null && cdi.size() > 0) {
            CustomerRecord.deleteAll(CustomerRecord.class);
        }
        customerRecordListAPI.CustomerRecordListAPICall();



    }


}
