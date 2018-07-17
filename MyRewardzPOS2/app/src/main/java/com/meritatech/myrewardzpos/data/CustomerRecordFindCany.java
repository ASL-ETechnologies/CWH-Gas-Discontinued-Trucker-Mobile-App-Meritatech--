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

public class CustomerRecordFindCany extends APIBase <CustomerRecord> {


    public void CustomerRecordFindCanyCall(String cAny)

    {

        final MyPosBase myPosBase = new MyPosBase();


        try {

            CustomerRecordFindCany reesponse;
            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<CustomerRecordFindCany> call = inventoryInterface.customerFindCany(GlobalVariables.realmId,GlobalVariables.token,cAny);

            call.enqueue(new Callback<CustomerRecordFindCany>() {
                @Override
                public void onResponse(Call<CustomerRecordFindCany> call, Response<CustomerRecordFindCany> response) {
                    CustomerRecordFindCany reesponse = response.body();

                    if (response.body().StatusCode == 0) {

                        ///  myPosBase.saveCustomer((List<CustomerRecord>) reesponse.Data);

                    }

                }

                @Override
                public void onFailure(Call<CustomerRecordFindCany> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

}
