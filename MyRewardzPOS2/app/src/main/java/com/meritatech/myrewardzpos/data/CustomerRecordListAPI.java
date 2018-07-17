package com.meritatech.myrewardzpos.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.utility.Utilities;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.dataObj.CustomerDataObj;
import com.meritatech.myrewardzpos.enums.ProcessingStatus;
import com.meritatech.myrewardzpos.global.CustomerActivityChecker;
import com.meritatech.myrewardzpos.interfaces.PosServicesInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Waithera on 12/1/2017.
 */

public class CustomerRecordListAPI extends APIBase<CustomerDataObj> {
    private Context context;

//save the context recievied via constructor in a local variable

    public CustomerRecordListAPI(Context context) {
        this.context = context;
    }

    int counter = 0;
    int totalRec = 100;
    final int limit = 1818;
    int loopInt = 0;
    MyPosBase myPosBase = new MyPosBase();

    public void CustomerRecordListAPICall() {

        ArrayList<CustomerDataInfoRecord> cdi = CustomerDataInfoRecord.findAllRecords(CustomerDataInfoRecord.class);
        long totalcustomersinLocaldb = CustomerRecord.count(CustomerRecord.class);
        if (cdi != null && cdi.size() > 0) {
            if (cdi.get(0).expectedtotalRecords > totalcustomersinLocaldb) {
                pollCustomerRecords();
            } else if (totalcustomersinLocaldb >= cdi.get(0).expectedtotalRecords) {
                if (GlobalVariables.custRefreshPeriodMins != null && Integer.parseInt(GlobalVariables.custRefreshPeriodMins) > 0) {
                    LoadContactsPeriodically();
                }
            }
        } else {
            pollCustomerRecords();
        }


    }

    public void GetExpectedRecNo() {
        try {
            PosServicesInterface inventoryInterface = ApiClient.getClient().create(PosServicesInterface.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String currentDateandTime = sdf.format(new Date());
            Call<CustomerRecordListAPI> call = inventoryInterface.customerListByTimeStamp(0, limit, "1", GlobalVariables.token, "19801101000000000000");
            CustomerActivityChecker.ProcessingStatus = ProcessingStatus.RUNNING;
            Response<CustomerRecordListAPI> response = call.execute();

            if (response.isSuccessful()) {
                try {
                    final CustomerRecordListAPI reesponse = response.body();
                    if (reesponse != null && reesponse.DataObj != null && reesponse.DataObj.totRecMatch > 0) {

                        ArrayList<CustomerDataInfoRecord> cdi = CustomerDataInfoRecord.findAllRecords(CustomerDataInfoRecord.class);
                        if (cdi == null || cdi.size() == 0) {
                            ArrayList<CustomerDataInfoRecord> cInfoList = new ArrayList<CustomerDataInfoRecord>();
                            CustomerDataInfoRecord cinfo = new CustomerDataInfoRecord();
                            cinfo.expectedtotalRecords = response.body().DataObj.totRecMatch;
                            cinfo.recNumber = 0;
                            cInfoList.add(cinfo);
                            myPosBase.saveCustomerDataInfo(cInfoList);
                        }

                        totalRec = response.body().DataObj.totRecMatch;
                    } else {
                        totalRec = 1;
                    }

                } catch (Exception e) {
                    Utilities.LogException(e);
                }


            } else {
                Utilities.LogException(new Exception(response.message()));

            }

        } catch (Exception ex) {
            Utilities.LogException(ex);
        }
    }

    public void pollCustomerRecords() {

        ArrayList<CustomerDataInfoRecord> cdi = CustomerDataInfoRecord.findAllRecords(CustomerDataInfoRecord.class);
        long totalcustomersinLocaldb = CustomerRecord.count(CustomerRecord.class);
        int totalLocRec = 0;
        if (cdi == null && cdi.size() == 0) {
            GetExpectedRecNo();
        }


        if (cdi != null && cdi.size() > 0) {
            totalLocRec = (cdi.get(0).expectedtotalRecords - (int) totalcustomersinLocaldb) / limit;
        } else {
            Log.e("wrong LOOP VALUE ", String.valueOf(totalRec));
            if (totalRec == 100) {
                GetExpectedRecNo();
            }
            totalLocRec = totalRec / limit;
        }
        loopInt = totalLocRec + 2;
        int i = 0;


        if (totalcustomersinLocaldb > 0 && cdi.get(0).expectedtotalRecords > totalcustomersinLocaldb) {
            totalLocRec = (cdi.get(0).expectedtotalRecords - (int) totalcustomersinLocaldb) / limit;
            loopInt = totalLocRec + 2;
            while (loopInt > 0) {

                final int start = (int) totalcustomersinLocaldb + (i * limit) + 1;
                SaveBatchToDisk(limit, start, "");
                i++;
                loopInt = loopInt - 1;
            }

        } else {
            while (loopInt > 0) {
                Log.e("i value", i + "");

                int start = (i * limit);
                SaveBatchToDisk(limit, start, "");
                loopInt = loopInt - 1;

                if (loopInt == 0) {
                    start = ((i + 1) * limit);
                    SaveBatchToDisk(limit, start, "");
                }
                i++;
            }
        }


    }


    public void SaveBatchToDisk(final int limit, final int start, final String startDate) {
        final MyPosBase myPosBase = new MyPosBase();
        try {
            CustomerRecordListAPI reesponse;
            PosServicesInterface inventoryInterface = ApiClient.getClient().create(PosServicesInterface.class);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
            String currentDateandTime = sdf.format(new Date());
            String lastDate = "";
            if (startDate == "" || startDate.isEmpty()) {
                lastDate = "19801101000000000000";
            } else {
                lastDate = startDate;
            }

            Call<CustomerRecordListAPI> call = inventoryInterface.customerListByTimeStamp(start, limit, "1", GlobalVariables.token, lastDate);
            CustomerActivityChecker.ProcessingStatus = ProcessingStatus.RUNNING;

            Response<CustomerRecordListAPI> response = call.execute();
            if (response.isSuccessful()) {
                final CustomerRecordListAPI reesponse1 = response.body();
                ArrayList<CustomerRecord> customerList = reesponse1.DataObj.Data;
                CustomerRecord customerRecord = new CustomerRecord();
                if (customerList.size() > 0) {
                    if (customerList.size() > 1) {
                        customerRecord = customerList.get(customerList.size() - 1);
                    } else {
                        customerRecord = customerList.get(0);
                    }
                }

                GlobalVariables.tStamp = customerRecord.tStamp;

                totalRec = reesponse1.DataObj.totRecMatch;
                if (response.body().StatusCode == 0) {

                    ArrayList<CustomerDataInfoRecord> cdi = CustomerDataInfoRecord.findAllRecords(CustomerDataInfoRecord.class);
                    if (cdi == null || cdi.size() == 0) {
                        ArrayList<CustomerDataInfoRecord> cInfoList = new ArrayList<CustomerDataInfoRecord>();
                        CustomerDataInfoRecord cinfo = new CustomerDataInfoRecord();


                        cinfo.entryTimestamp = customerRecord.tStamp;
                        cinfo.expectedtotalRecords = response.body().DataObj.totRecMatch;
                        cinfo.recNumber = 0;
                        cInfoList.add(cinfo);
                        Log.e("NEW DATE", customerRecord.tStamp);
                        myPosBase.saveCustomerDataInfo(cInfoList);
                    } else {

                        CustomerDataInfoRecord cinfo = new CustomerDataInfoRecord();
                        cinfo.entryTimestamp = customerRecord.tStamp;
                        cinfo.expectedtotalRecords = response.body().DataObj.totRecMatch;
                        cinfo.recNumber = 0;

                        myPosBase.UpdateCustomerInfo(cinfo, null);
                    }


                    Thread.currentThread().setName("Save Customer Thread");
                    counter = counter + reesponse1.DataObj.Data.size();
                    myPosBase.saveCustomer((List<CustomerRecord>) reesponse1.DataObj.Data);


                    checkIfSaveIsComplete();
                }
            } else {
                Utilities.LogException(new Exception(response.message()));
            }

        } catch (Exception ex) {
            Utilities.LogException(ex);
        }


    }

    public void checkIfSaveIsComplete() {

        long cusRec = CustomerRecord.count(CustomerRecord.class);
        ArrayList<CustomerDataInfoRecord> cdi = CustomerDataInfoRecord.findAllRecords(CustomerDataInfoRecord.class);
        final CustomerDataInfoRecord rec = new CustomerDataInfoRecord();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
        String currentDateandTime = sdf.format(new Date());

        rec.entryTimestamp = GlobalVariables.tStamp;
        rec.recNumber = (int) cusRec;
        if (cdi != null && cdi.size() > 0) {
            myPosBase.UpdateCustomerInfo(rec, cdi.get(0).getObjectID());
        }

        Log.i("RecInfo", "RecCount" + rec.recNumber);

        if (totalRec == cusRec) {
            if (GlobalVariables.custRefreshPeriodMins != null && Integer.parseInt(GlobalVariables.custRefreshPeriodMins) > 0) {

            }

            CustomerActivityChecker.IsCustomerRecordsReady = true;
            CustomerActivityChecker.ProcessingStatus = ProcessingStatus.FINISHED;
        }
    }

    public void LoadContactsPeriodically() {
        try {

            Handler handler = new Handler(Looper.getMainLooper());

            handler.post(new Runnable() {

                @Override
                public void run() {
                    int duration = Toast.LENGTH_LONG;
                    String recordinfo = "Hourly customer polling...";
                    Toast toast = Toast.makeText(context.getApplicationContext(), recordinfo, duration);
                    toast.show();
                }
            });

            ArrayList<CustomerDataInfoRecord> cdi = CustomerDataInfoRecord.findAllRecords(CustomerDataInfoRecord.class);
            CustomerDataInfoRecord customerDataInfoRecord;
            if (cdi.size() > 1) {
                customerDataInfoRecord = cdi.get(cdi.size() - 1);

            } else {
                customerDataInfoRecord = cdi.get(0);
            }

            ArrayList<CustomerRecord> cus = myPosBase.GetlastCustomer();
            if (cus != null) {
                SaveBatchToDisk(100, 0, cus.get(0).tStamp);
                Log.e("IsPolling", customerDataInfoRecord.entryTimestamp + "");
            }
        } catch (Exception ex) {
            Utilities.LogException(ex);
        }
    }


    public void SaveUserDetails() {
        try {
            UserRecord.deleteAll(UserRecord.class);
            UserRecord userRecord = new UserRecord();
            userRecord.salesmanId = GlobalVariables.salesmanId;
            userRecord.invDwnldTime = GlobalVariables.invDwnldTime;
            userRecord.custDwnldTime = GlobalVariables.custDwnldTime;
            userRecord.soDwnldPeriodMins = GlobalVariables.soDwnldPeriodMins;
            userRecord.sTxnDwnldPeriodMins = GlobalVariables.sTxnDwnldPeriodMins;
            userRecord.parentStoreId = GlobalVariables.ParentId;
            userRecord.storeId = GlobalVariables.storeId;
            userRecord.realmId = GlobalVariables.realmId;
            userRecord.userPassword = GlobalVariables.userPassword;
            userRecord.pointsPerDollar = Integer.valueOf(GlobalVariables.pointsPerDollar);
            ArrayList<UserRecord> crecord = new ArrayList<UserRecord>();
            crecord.add(userRecord);
            UserRecord.save(userRecord);
            //myPosBase.saveUserRecord(crecord);

            ArrayList<UserRecord> usrs = UserRecord.findAllRecords(UserRecord.class);
        } catch (Exception ex) {
            Utilities.LogException(ex);
        }
    }

}
