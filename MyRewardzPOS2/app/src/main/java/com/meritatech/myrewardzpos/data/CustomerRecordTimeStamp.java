package com.meritatech.myrewardzpos.data;

import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.model.PosServicesInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Waithera on 12/1/2017.
 */

public class CustomerRecordTimeStamp extends APIBase<List<CustomerRecord>> {


    public void CustomerRecordTimeStampCall(String timestamp,String position,String numberOfRecords)

    {

        final MyPosBase myPosBase = new MyPosBase();


        try {

            CustomerRecordTimeStamp reesponse;
            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<CustomerRecordTimeStamp> call = inventoryInterface.customerSingleByTimeStamp(GlobalVariables.realmId,GlobalVariables.token,timestamp,position,numberOfRecords);

            call.enqueue(new Callback<CustomerRecordTimeStamp>() {
                @Override
                public void onResponse(Call<CustomerRecordTimeStamp> call, Response<CustomerRecordTimeStamp> response) {
                    CustomerRecordTimeStamp reesponse = response.body();

                    if (response.body().StatusCode == 0) {

                      //  myPosBase.saveCustomer((List<CustomerRecord>) reesponse.Data);

                    }

                }

                @Override
                public void onFailure(Call<CustomerRecordTimeStamp> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


}
