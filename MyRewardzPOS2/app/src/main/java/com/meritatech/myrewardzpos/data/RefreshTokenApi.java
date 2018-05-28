package com.meritatech.myrewardzpos.data;

import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.model.PosServicesInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Waithera on 12/17/2017.
 */

public class RefreshTokenApi extends APIBase<RefreshTokenRecord> {

    public void RefreshToken()

    {

        final MyPosBase myPosBase= new MyPosBase();


        try {

            final RefreshTokenApi reesponse;
            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<RefreshTokenApi> call = inventoryInterface.refreshToken(GlobalVariables.token);

            call.enqueue(new Callback<RefreshTokenApi>() {
                @Override
                public void onResponse(Call<RefreshTokenApi> call, Response<RefreshTokenApi> response) {
                  //  Response<APIBase> reesponse = response;


                    if(response.body().StatusCode==0) {


                   //     GlobalVariables.token=response.body().Data.newtoken;
                    }
                }

                @Override
                public void onFailure(Call<RefreshTokenApi>call, Throwable t) {

                }
            });
        }catch (Exception ex)
        {
            ex.getMessage();
        }

    }


}
