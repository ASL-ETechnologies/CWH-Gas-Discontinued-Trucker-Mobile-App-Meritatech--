package com.meritatech.myrewardzpos.data;

import com.google.gson.Gson;
import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.dataObj.LogsDataObj;
import com.meritatech.myrewardzpos.model.LogsDataModel;
import com.meritatech.myrewardzpos.utility.Utilities;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;
import com.meritatech.myrewardzpos.model.InvoiceDataModel;
import com.meritatech.myrewardzpos.interfaces.PosServicesInterface;

import retrofit2.Call;
import retrofit2.Response;


public class SalesTransactionsAPI extends APIBase<InvoiceDataObj> {


    public void SalesTransactionWrite(final InvoiceDataObj dataObj) {
        try {

            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<InvoiceDataModel> call = inventoryInterface.saveInvoice(GlobalVariables.token, dataObj);
            Response<InvoiceDataModel> response = call.execute();
            if (response.isSuccessful() == false) {
                Utilities.LogError("Record return unsuccessful in transaction write");
            }
            for (int i = 0; i < dataObj.Data.size(); i++) {
                if (dataObj.Data.get(i).recID > 0) {
                    SalesInvoiceRecord rec = SalesInvoiceRecord.findById(SalesInvoiceRecord.class, dataObj.Data.get(i).recID);
                    rec.sent = 1;
                    rec.save();
                    rec = SalesInvoiceRecord.findById(SalesInvoiceRecord.class, dataObj.Data.get(i).recID);

                    if (rec.sent != 1) {
                        throw new Exception("Invalid value");
                    }
                }
            }


        } catch (Exception ex) {
            Utilities.LogException(ex);
        }

    }

    public void PostLogs(final LogsDataObj dataObj) {
        try {
            Gson gson1 = new Gson();
            String jsonObj = gson1.toJson(dataObj.logRecord);
            PosServicesInterface posInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<LogsDataModel> call = posInterface.saveLogs(GlobalVariables.token, jsonObj);
            Response<LogsDataModel> response = call.execute();
            if (response.isSuccessful() == false) {
                Utilities.LogError("Record return unsuccessful in logs write");
            }
            else {

                for (int i = 0; i < dataObj.logRecord.size(); i++) {

                    LogRecord rec = SalesInvoiceRecord.findById(LogRecord.class, dataObj.logRecord.get(i).getId());
                        rec.sent = 1;
                        rec.save();
                        rec = LogRecord.findById(LogRecord.class, dataObj.logRecord.get(i).getId());

                        if (rec.sent != 1) {
                            throw new Exception("Invalid value");
                        }

                }
            }


        } catch (Exception ex) {
            Utilities.LogException(ex);
        }

    }
}
