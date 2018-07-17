package com.meritatech.myrewardzpos.data;

//import com.meritatech.myrewardzpos.Db.InventoryDAO;

import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.database.SugarRecord;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Waithera on 12/1/2017.
 */

public class MyPosBase extends SugarRecord {

    boolean isSuccess = false;


    public boolean saveInventory(List<InventoryRecord> inventory) {


        try {


            saveInTx(inventory);


            isSuccess = true;


        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return isSuccess;
    }


    //this is for an invoice
    public boolean saveInvoiceDetails(Inventory inventory) {


        try {


            saveInTx(inventory);


            isSuccess = true;


        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public CustomerRecord GetCustomerByID(String cusId) {
        CustomerRecord result = (CustomerRecord) SugarRecord.find(CustomerRecord.class, "CUSTOMER_ID = ?", cusId);
        return result;
    }

    public ArrayList<CustomerRecord> GetlastCustomer() {
        ArrayList<CustomerRecord> result = (ArrayList<CustomerRecord>) CustomerRecord.findWithQuery(CustomerRecord.class, "Select * from CUSTOMER_RECORD order by T_STAMP desc limit 1 ");
        return result;

    }

    public boolean saveCustomer(List<CustomerRecord> customer) {

        try {
            saveInTx(customer);
            isSuccess = true;
        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean saveUserRecord(ArrayList<UserRecord> userRecords) {

        try {
            saveInTx(userRecords);
            isSuccess = true;
        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean saveCustomerDataInfo(ArrayList<CustomerDataInfoRecord> customerRecInfo) {

        try {
            saveInTx(customerRecInfo);
            isSuccess = true;
        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return isSuccess;
    }


    public static void UpdateCustomerInfo(CustomerDataInfoRecord customerDataInfoRecord, UUID uuid) {
        ArrayList<CustomerDataInfoRecord> all = CustomerDataInfoRecord.findAllRecords(CustomerDataInfoRecord.class);
        CustomerDataInfoRecord frecord = all.get(0);
        frecord.recNumber = customerDataInfoRecord.recNumber;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
        String currentDateandTime = sdf.format(new Date());
        frecord.entryTimestamp = customerDataInfoRecord.entryTimestamp;
        frecord.save();

    }


    public boolean saveSalesOrder(ArrayList<SalesOrderRecord> salesOrderRecords) {

        try {
            saveInTx(salesOrderRecords);
            isSuccess = true;
        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return isSuccess;
    }


    public boolean saveSalesOrderInventory(ArrayList<SalesOrderInventoryRecord> salesOrderInventoryRecords) {

        try {
            saveInTx(salesOrderInventoryRecords);
            isSuccess = true;
        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return isSuccess;
    }


    public boolean saveSalesOrderDetails(List<SalesOrderDetailsRecord> salesOrderDetailsRecords) {

        try {
            saveInTx(salesOrderDetailsRecords);
            isSuccess = true;
        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean saveInvoice(SalesInvoiceRecord salesInvoiceRecord) {
        try {
            saveInTx(salesInvoiceRecord);
            ArrayList<SalesInvoiceRecord> listOfSalesInvoice = SalesInvoiceRecord.findAllRecords(SalesInvoiceRecord.class);
            isSuccess = true;

        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean saveInvoiceInventory(ArrayList<SalesInvoiceInventoryRecord> salesInvoiceInventoryRecord) {
        try {
            saveInTx(salesInvoiceInventoryRecord);
            isSuccess = true;

        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return isSuccess;
    }


    ///select functions
    public List<InventoryRecord> SelectInventory() {
        List<InventoryRecord> iinventoryList = null;


        try {


            iinventoryList = (List<InventoryRecord>) All(InventoryRecord.class);


            isSuccess = true;


        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return iinventoryList;
    }


    public List<SalesRecords> SelectSalesOrder() {
        List<SalesRecords> salesOrderList = null;


        try {


            salesOrderList = (List<SalesRecords>) All(SalesRecords.class);


            isSuccess = true;


        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return salesOrderList;
    }


    public List<CustomerRecord> SelectCustomer() {
        List<CustomerRecord> customerList = null;


        try {


            //   customerList = (List<CustomerRecord>) AllCustomer(CustomerRecord.class);
            customerList = (List<CustomerRecord>) All(CustomerRecord.class);


            isSuccess = true;


        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return customerList;
    }


    public List<SalesOrderRecord> SelectInvoice() {
        List<SalesOrderRecord> salesOrderRecords = null;
        try {
            salesOrderRecords = findAllTosendToServer(0);
            isSuccess = true;
        } catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return salesOrderRecords;
    }


    public static ArrayList<InventoryRecord> findAllWithinViewPort(String sku) {
        ArrayList<InventoryRecord> result = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("ITEM_NUM = %s", sku));


        String whereClause = builder.toString();
        for (Iterator<InventoryRecord> it = DatabaseObject.findAsIterator(InventoryRecord.class, whereClause);
             it.hasNext(); ) {
            InventoryRecord inventRecord = it.next();
            inventRecord.afterLoad();
            result.add(inventRecord);
        }
        return result;
    }


    public static ArrayList<SalesOrderRecord> findAllTosendToServer(int sentToServer) {
        ArrayList<SalesOrderRecord> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("SENT = %d", sentToServer));
        String whereClause = builder.toString();
        for (Iterator<SalesOrderRecord> it = DatabaseObject.findAsIterator(SalesOrderRecord.class, whereClause);
             it.hasNext(); ) {
            SalesOrderRecord salesOrderRecord = it.next();
            salesOrderRecord.afterLoad();
            result.add(salesOrderRecord);
        }
        return result;
    }

    public static InventoryRecord GetInventoryByItemNo(String itemNumber) {
        InventoryRecord result = new InventoryRecord();
        ArrayList<InventoryRecord> it = (ArrayList<InventoryRecord>) SugarRecord.find(InventoryRecord.class, "ITEM_NUM = ?", itemNumber);
        if (it != null && it.size() > 0) {
            result = it.get(0);
        }
        return result;
    }


    public static ArrayList<SalesInvoiceInventoryRecord> GetLocalInventoryById(String salesorderid) {
        ArrayList<SalesInvoiceInventoryRecord> result = (ArrayList<SalesInvoiceInventoryRecord>) SugarRecord.find(SalesInvoiceInventoryRecord.class, "SALES_ORDER_ID = ?", salesorderid);
        return result;
    }


    public static ArrayList<LogRecord> GetLogs(String totalItemCount) {
        boolean isOp = checkIsOpen();
        ArrayList<LogRecord> result = (ArrayList<LogRecord>) LogRecord.findWithQuery(LogRecord.class, "Select * from LOG_RECORD ORDER BY ID LIMIT ? OFFSET ?", "20", totalItemCount);
        return result;
    }

    public static ArrayList<CustomerRecord> GetCustomers(String totalItemCount) {
        boolean isOp = checkIsOpen();

        ArrayList<CustomerRecord> result = (ArrayList<CustomerRecord>) CustomerRecord.findWithQuery(CustomerRecord.class, "Select * from CUSTOMER_RECORD ORDER BY ID LIMIT ? OFFSET ?", "20", totalItemCount);
        return result;
    }

    public static ArrayList<UserRecord> GetUserDetails() {
        ArrayList<UserRecord> result = (ArrayList<UserRecord>) UserRecord.findWithQuery(UserRecord.class, "Select * from USER_RECORD");
        return result;
    }


    public static ArrayList<SalesOrderInventoryRecord> GetSalesOrderInventoryRecords(String salesorderid) {
        ArrayList<SalesOrderInventoryRecord> result = (ArrayList<SalesOrderInventoryRecord>) SalesOrderInventoryRecord.findWithQuery(SalesOrderInventoryRecord.class, "Select * from SALES_ORDER_INVENTORY_RECORD where SALES_ORDER_ID = ?", salesorderid);
        return result;
    }

    public static ArrayList<SalesInvoiceInventoryRecord> GetSalesInvoiceInventoryRecords(String salesorderid) {
        ArrayList<SalesInvoiceInventoryRecord> result = (ArrayList<SalesInvoiceInventoryRecord>) SalesInvoiceInventoryRecord.findWithQuery(SalesInvoiceInventoryRecord.class, "Select * from SALES_INVOICE_INVENTORY_RECORD where SALES_ORDER_ID = ?", salesorderid);
        return result;
    }

    public static CustomerRecord GetCustomerRecord(String customerId) {
        ArrayList<CustomerRecord> result = (ArrayList<CustomerRecord>) CustomerRecord.findWithQuery(CustomerRecord.class, "Select * from CUSTOMER_RECORD where CUSTOMER_ID = ?", customerId);
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }

    }

    public static ArrayList<CustomerRecord> SearchCustomer(String searchstr, String emailstr, String phone1) {
        ArrayList<CustomerRecord> result = new ArrayList<CustomerRecord>();
        if (searchstr.length() > 0 && emailstr.length() == 0 && phone1.length() == 0) {
            if (searchstr.length() > 0 && searchstr.contains(" ") && emailstr.length() == 0 && phone1.length() == 0) {
                result = (ArrayList<CustomerRecord>) CustomerRecord.findWithQuery(CustomerRecord.class, "Select *  from CUSTOMER_RECORD where F_NAME || ' ' || L_NAME like ?", "%" + searchstr + "%");
                ArrayList<CustomerRecord> result2 = new ArrayList<CustomerRecord>();
                result2 = (ArrayList<CustomerRecord>) CustomerRecord.findWithQuery(CustomerRecord.class, "Select *  from CUSTOMER_RECORD where L_NAME || ' ' || F_NAME like ?", "%" + searchstr + "%");
                if (result2.size() > 0) {
                    for (int i = 0; i < result2.size(); i++) {
                        result.add(result2.get(i));
                    }

                }

            } else {
                result = (ArrayList<CustomerRecord>) CustomerRecord.findWithQuery(CustomerRecord.class, "Select * from CUSTOMER_RECORD where F_NAME like ? or L_NAME like ?", "%" + searchstr + "%");
            }

        } else if (searchstr.length() > 0 && emailstr.length() > 0 && phone1.length() == 0) {
            result = (ArrayList<CustomerRecord>) CustomerRecord.findWithQuery(CustomerRecord.class, "Select * from CUSTOMER_RECORD where F_NAME like ? or L_NAME like ? and EMAIL like?", "%" + searchstr + "%", "%" + searchstr + "%", "%" + emailstr + "%");

        } else if (searchstr.length() == 0 && emailstr.length() > 0 && phone1.length() == 0) {
            result = (ArrayList<CustomerRecord>) CustomerRecord.findWithQuery(CustomerRecord.class, "Select * from CUSTOMER_RECORD where EMAIL like? ", "%" + emailstr + "%");

        } else if (searchstr.length() > 0 && emailstr.length() == 0 && phone1.length() > 0) {
            result = (ArrayList<CustomerRecord>) CustomerRecord.findWithQuery(CustomerRecord.class, "Select * from CUSTOMER_RECORD where F_NAME like ? or L_NAME like ? and PHONE1 like? or PHONE2 like ? ", "%" + searchstr + "%", "%" + phone1 + "%");

        } else if (searchstr.length() == 0 && emailstr.length() == 0 && phone1.length() > 0) {
            result = (ArrayList<CustomerRecord>) CustomerRecord.findWithQuery(CustomerRecord.class, "Select * from CUSTOMER_RECORD where PHONE1 like? or PHONE2 like ? ", "%" + phone1 + "%");

        } else if (searchstr.length() > 0 && emailstr.length() > 0 && phone1.length() > 0) {
            result = (ArrayList<CustomerRecord>) CustomerRecord.findWithQuery(CustomerRecord.class, "Select * from CUSTOMER_RECORD where F_NAME like ? or L_NAME like ? and EMAIL like? and PHONE1 like? or PHONE2 like ? ", "%" + searchstr + "%", "%" + emailstr + "%", "%" + phone1 + "%");

        }

        if (result.size() > 0) {
            return result;
        } else {
            return null;
        }

    }


    public static ArrayList<CustomerRecord> findAllCustomerDetails(String customerId) {
        ArrayList<CustomerRecord> result = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("CUSTOMER_ID = %s", customerId));


        String whereClause = builder.toString();
        for (Iterator<CustomerRecord> it = DatabaseObject.findAsIterator(CustomerRecord.class, whereClause);
             it.hasNext(); ) {
            CustomerRecord customerRecord = it.next();
            customerRecord.afterLoad();
            result.add(customerRecord);
        }
        return result;
    }


    public static ArrayList<Inventory> findAllSalesDetails(String orderId) {
        ArrayList<Inventory> result = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("LINKER_ORDER_ID = %s", orderId));


        String whereClause = builder.toString();
        for (Iterator<Inventory> it = DatabaseObject.findAsIterator(Inventory.class, whereClause);
             it.hasNext(); ) {
            Inventory inventory = it.next();
            inventory.afterLoad();
            result.add(inventory);
        }
        return result;
    }

    public static ArrayList<SalesInvoiceRecord> GetUnSentTransactions(String sent) {
        ArrayList<SalesInvoiceRecord> result = (ArrayList<SalesInvoiceRecord>) SugarRecord.find(SalesInvoiceRecord.class, "SENT = ?", sent);
        return result;
    }


    public static ArrayList<LogRecord> GetUnSentLogs(String sent) {
        ArrayList<LogRecord> result = (ArrayList<LogRecord>) SugarRecord.find(LogRecord.class, "SENT = ?", sent);
        return result;
    }


    public static ArrayList<SalesInvoiceRecord> GetCustomerPointsHistoryOnCreate(String customerid) {

        ArrayList<SalesInvoiceRecord> result = (ArrayList<SalesInvoiceRecord>) SalesInvoiceRecord.findWithQuery(SalesInvoiceRecord.class, "Select * from SALES_INVOICE_RECORD WHERE CUSTOMER_ID =" + customerid + " AND SALES_ORDER_STATUS = ORIGINAL order by ID desc limit 1 ");
        return result;
    }

    public static ArrayList<SalesInvoiceRecord> GetCustomerPointsHistory(String customerid) {

        ArrayList<SalesInvoiceRecord> result = (ArrayList<SalesInvoiceRecord>) SalesInvoiceRecord.findWithQuery(SalesInvoiceRecord.class, "Select * from SALES_INVOICE_RECORD WHERE CUSTOMER_ID =" + customerid + " order by ID desc limit 1 ");
        return result;
    }

    public static SalesInvoiceRecord GetTransactionById(String id) {
        SalesInvoiceRecord result = (SalesInvoiceRecord) SugarRecord.find(SalesInvoiceRecord.class, "salesOrderId = ?", id);
        return result;
    }


}


