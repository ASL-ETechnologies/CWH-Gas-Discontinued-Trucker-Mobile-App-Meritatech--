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

public class CustomerRecordAPI extends APIBase<CustomerRecord> {

    public void CustomerRecordAPICall(String customerId)

    {

        final MyPosBase myPosBase = new MyPosBase();


        try {

            CustomerRecordAPI reesponse;
            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<CustomerRecordAPI> call = inventoryInterface.customerSingle(GlobalVariables.realmId,customerId,GlobalVariables.token);

            call.enqueue(new Callback<CustomerRecordAPI>() {
                @Override
                public void onResponse(Call<CustomerRecordAPI> call, Response<CustomerRecordAPI> response) {
                    CustomerRecordAPI reesponse = response.body();

                    if (response.body().StatusCode == 0) {

                    //    myPosBase.saveCustomer((List<CustomerRecord>) reesponse.Data);

                    }

                }

                @Override
                public void onFailure(Call<CustomerRecordAPI> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }
}
