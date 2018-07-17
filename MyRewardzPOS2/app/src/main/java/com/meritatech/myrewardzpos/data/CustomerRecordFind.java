package com.meritatech.myrewardzpos.data;

import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.interfaces.PosServicesInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Waithera on 12/15/2017.
 */

public class CustomerRecordFind extends APIBase<CustomerRecord> {


    public void CustomerRecordFindCall(String cEmail,String cTell,String cAddress,String cName)

    {

        final MyPosBase myPosBase = new MyPosBase();


        try {

            CustomerRecordFind reesponse;
            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<CustomerRecordFind> call = inventoryInterface.customerFind(GlobalVariables.realmId,GlobalVariables.token,cEmail,cTell,cAddress,cName);

            call.enqueue(new Callback<CustomerRecordFind>() {
                @Override
                public void onResponse(Call<CustomerRecordFind> call, Response<CustomerRecordFind> response) {
                    CustomerRecordFind reesponse = response.body();

                    if (response.body().StatusCode == 0) {

                      ///  myPosBase.saveCustomer((List<CustomerRecord>) reesponse.Data);

                    }

                }

                @Override
                public void onFailure(Call<CustomerRecordFind> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }
}
