package com.meritatech.myrewardzpos;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.controller.Inventory;
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
import com.meritatech.myrewardzpos.data.UserRecord;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;
import com.meritatech.myrewardzpos.database.SugarContext;
import com.meritatech.myrewardzpos.database.SugarRecord;
import com.meritatech.myrewardzpos.enums.ProcessingStatus;
import com.meritatech.myrewardzpos.global.CustomerActivityChecker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.http.PUT;

import static com.meritatech.myrewardzpos.MainActivity.appContext;

/**
 * Created by Waithera on 12/2/2017.
 */

public class UploadBackGroundService extends IntentService {

    private Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    NetworkInfo wifiCheck;
    UserRecord userRecord;

    public UploadBackGroundService() {
        // Used to name the worker thread, important only for debugging.
        super("backgound-service");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            if (appContext != null) {
                SugarContext.init(appContext);
            } else {
                appContext = getApplicationContext();
                if (appContext != null) {
                    SugarContext.init(appContext);
                }

            }

            Utilities utilities = new Utilities();
            utilities.InitializeDB(appContext);
            if (GlobalVariables.isConnected(getApplicationContext())) {
                InventoryListAPI inventoryListAPI = new InventoryListAPI();
                inventoryListAPI.InventoryListAPICall();

                CustomerRecordListAPI customerRecordListAPI = new CustomerRecordListAPI(appContext);
                ArrayList<UserRecord> existinUser = UserRecord.findAllRecords(UserRecord.class);
                if (existinUser != null && existinUser.size() > 0) {
                    if (!existinUser.get(0).salesmanId.equals(GlobalVariables.salesmanId)) {
                        SalesOrderRecord.deleteAll(SalesOrderRecord.class);
                        InventoryRecord.deleteAll(InventoryRecord.class);
                    }
                }

                customerRecordListAPI.SaveUserDetails();
                SalesOrderRecordListAPI salesOrderRecordListAPI = new SalesOrderRecordListAPI();
                salesOrderRecordListAPI.SalesOrderRecordListAPICall();
                userRecord = UserRecord.findAllRecords(UserRecord.class).get(0);
/*set expected contacts records to localdb*/
                customerRecordListAPI.GetExpectedRecNo();
                Thread.sleep(10000);
                deleteOldSalesHistory();
                pollInventory();
                pollSalesOrders();
                runInBackground();

                ArrayList<CustomerDataInfoRecord> cdi = CustomerDataInfoRecord.findAllRecords(CustomerDataInfoRecord.class);
                long totalcustomersinLocaldb = CustomerRecord.count(CustomerRecord.class);
                if (cdi != null && cdi.size() > 0) {
                    if (totalcustomersinLocaldb >= cdi.get(0).expectedtotalRecords) {
                        if (GlobalVariables.custRefreshPeriodMins != null && Integer.parseInt(GlobalVariables.custRefreshPeriodMins) > 0) {
                            customerRecordListAPI.LoadContactsPeriodically();
                        }
                    }
                    else {
                        pollCustomers();
                    }
                }

            }

        } catch (Exception ex) {
            Utilities.LogException(ex);
        }
    }


    ScheduledExecutorService scheduleTaskExecutor =
            Executors.newSingleThreadScheduledExecutor();

    public void pollSalesOrders() {
        try {
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    ArrayList<SalesOrderRecord> allsalesorders = SalesOrderRecord.findAllRecords(SalesOrderRecord.class);
                    if (allsalesorders != null && allsalesorders.size() > 0 && GlobalVariables.isConnected(getBaseContext())) {
                        SalesOrderRecord.deleteAll(SalesOrderRecord.class);
                        SalesOrderInventoryRecord.deleteAll(SalesOrderInventoryRecord.class);
                    }
                    SalesOrderRecordListAPI salesOrderRecordListAPI = new SalesOrderRecordListAPI();
                    salesOrderRecordListAPI.SalesOrderRecordListAPICall();
                }
            }, 0, Long.parseLong(userRecord.soDwnldPeriodMins), TimeUnit.MINUTES);

        } catch (Exception ex) {
            Utilities.LogException(ex);
        }
    }


    public void pollCustomers() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                try {
                    ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (wifiCheck.isConnected()) {

                        long cusRec = CustomerRecord.count(CustomerRecord.class);
                        ArrayList<CustomerDataInfoRecord> cdi = CustomerDataInfoRecord.findAllRecords(CustomerDataInfoRecord.class);

                        if (cusRec == 0 || cdi.get(0).expectedtotalRecords > cusRec) {

                            long customers = CustomerRecord.count(CustomerRecord.class);

                            CustomerRecordListAPI customerRecordListAPI = new CustomerRecordListAPI(appContext);
                            customerRecordListAPI.CustomerRecordListAPICall();


                        }
                    } else {
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                int duration = Toast.LENGTH_LONG;
                                String recordinfo = "Customers will sync on Wifi connection!";
                                Toast toast = Toast.makeText(getApplicationContext(), recordinfo, duration);
                                toast.show();
                            }
                        });


                    }
                } catch (Exception e) {
                    Utilities.LogException(e);
                }

            }
        });
    }

    public void deleteOldSalesHistory() {
        ArrayList<SalesInvoiceRecord> allSales = SalesInvoiceRecord.findAllRecords(SalesInvoiceRecord.class);
        if (allSales != null && allSales.size() > 0) {
            for (int i = 0; i < allSales.size(); i++) {
                String invoiceStart = allSales.get(i).invoiceDateTime;
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                try {
                    Date todaysDate = new java.util.Date();
                    Date invoiceDate = format.parse(invoiceStart);
                    long diff = todaysDate.getTime() - invoiceDate.getTime();
                    long differenceDates = diff / (60 * 60 * 1000); // difference in hours
                    if (GlobalVariables.invSoPurgeAgeDays == null) {
                        GlobalVariables.invSoPurgeAgeDays = "0";
                    }
                    int maxhours = 24 * Integer.parseInt(GlobalVariables.invSoPurgeAgeDays);
                    if (differenceDates > maxhours) {
                        allSales.get(i).delete();
                    }
                } catch (Exception e) {
                    Utilities.LogException(e);
                }
            }
            new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        }

    }

    public void pollInventory() {
        try {
            DateFormat df = new SimpleDateFormat("hh:mm:ss");
            Calendar cal = Calendar.getInstance();
            String s = df.format(cal.getTime());
            long date1 = df.parse(s).getTime();
            long date2 = 0;
            date2 = df.parse(userRecord.invDwnldTime).getTime();
            long diff = date1 - date2;
            long diffInsec = diff / 1000;
            long diffInHours = diffInsec / 3600;

            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    ArrayList<InventoryRecord> ir = InventoryRecord.findAllRecords(InventoryRecord.class);
                    if (ir != null && ir.size() > 0 && GlobalVariables.isConnected(getBaseContext())) {
                        InventoryRecord.deleteAll(InventoryRecord.class);
                    }
                    InventoryListAPI inventoryListAPI = new InventoryListAPI();
                    inventoryListAPI.InventoryListAPICall();
                }
            }, 0, 24, TimeUnit.HOURS);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void pollSalesTransactions() {
        try {
            final MyPosBase myPosBase = new MyPosBase();
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<SalesInvoiceRecord> allsalestransactions = myPosBase.GetUnSentTransactions("0");
                            if (allsalestransactions != null && allsalestransactions.size() > 0 && GlobalVariables.isConnected(getBaseContext())) {
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
                                    salesOrderPost1.recID = allsalestransactions.get(i).recId;
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
                        }
                    });
                }

            }, 0, 1, TimeUnit.MINUTES);

        } catch (Exception ex) {
            Utilities.LogException(ex);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static boolean isRecursionEnable = false;

    void runInBackground() {
        if (isRecursionEnable)
            return;    // Handle not to start multiple parallel threads
        isRecursionEnable = true; // on exception on thread make it true again
        new Thread(new Thread() {
            @Override
            public void run() {
                pollSalesTransactions();
            }
        }).start();
    }
}
