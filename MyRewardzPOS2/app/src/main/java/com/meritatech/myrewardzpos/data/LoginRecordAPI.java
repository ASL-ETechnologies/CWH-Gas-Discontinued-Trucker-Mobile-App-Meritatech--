package com.meritatech.myrewardzpos.data;

import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.model.PosServicesInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Waithera on 12/1/2017.
 */

public class LoginRecordAPI extends  APIBase<LoginRecord> {


    public void LoginRecordAPICall(String salesManid,String passWord)

    {

        final MyPosBase myPosBase = new MyPosBase();


        try {

            LoginRecordAPI reesponse;
            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<LoginRecordAPI> call = inventoryInterface.loginRequest(salesManid,passWord);

            call.enqueue(new Callback<LoginRecordAPI>() {
                @Override
                public void onResponse(Call<LoginRecordAPI> call, Response<LoginRecordAPI> response) {
                    LoginRecordAPI reesponse = response.body();

                    if (response.body().StatusCode == 0) {



                        //myPosBase.saveSalesOrder((List<LoginRecord>) reesponse.Data);
                        //okay for loginInterface

                    }

                }

                @Override
                public void onFailure(Call<LoginRecordAPI> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }

    }

}



