package com.meritatech.myrewardzpos.data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.Utilities;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;
import com.meritatech.myrewardzpos.model.InvoiceDataModel;
import com.meritatech.myrewardzpos.model.PosServicesInterface;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SalesTransactionsAPI extends APIBase<InvoiceDataObj> {


    public void SalesTransactionWrite(final InvoiceDataObj dataObj) {
              try {
            Gson gson1 = new Gson();
            String jsonObj = gson1.toJson(dataObj);
            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<InvoiceDataModel> call = inventoryInterface.saveInvoice(GlobalVariables.token, dataObj);
            call.enqueue(new Callback<InvoiceDataModel>() {
                @Override
                public void onResponse(Call<InvoiceDataModel> call, Response<InvoiceDataModel> response) {
                    try {
                        for (int i = 0; i< dataObj.Data.size(); i++)
                        {

                            if(dataObj.Data.get(i).recID > 0) {
                                SalesInvoiceRecord rec = SalesInvoiceRecord.findById(SalesInvoiceRecord.class, dataObj.Data.get(i).recID);
                                rec.sent = 1;
                                rec.save();
                            }
                        }



                    } catch (Exception e) {
                        Utilities.LogException(e);
                    }
                }

                @Override
                public void onFailure(Call<InvoiceDataModel> call, Throwable t) {
                    try {
                        int i = 0;
                    } catch (Exception e) {
                        Utilities.LogException(e);
                    }
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }

    }
}
