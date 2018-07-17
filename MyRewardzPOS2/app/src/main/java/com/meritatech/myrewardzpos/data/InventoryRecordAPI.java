package com.meritatech.myrewardzpos.data;

import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.dataObj.InventoryDataObj;
import com.meritatech.myrewardzpos.interfaces.PosServicesInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Waithera on 12/1/2017.
 */

public class InventoryRecordAPI extends APIBase<InventoryDataObj>{

    public void InventoryRecordAPICall( String classId,String sku )

    {

        final MyPosBase myPosBase = new MyPosBase();


        try {

            InventoryRecordAPI reesponse;
            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<InventoryRecordAPI> call = inventoryInterface.inventorySingle(GlobalVariables.ParentId,GlobalVariables.storeId,classId,sku,GlobalVariables.token);

            call.enqueue(new Callback<InventoryRecordAPI>() {
                @Override
                public void onResponse(Call<InventoryRecordAPI> call, Response<InventoryRecordAPI> response) {
                    InventoryRecordAPI reesponse = response.body();

                    if (response.body().StatusCode == 0) {

                   //     myPosBase.saveInventory((List<InventoryRecord>) reesponse.DataObj.Data);


                    }

                }

                @Override
                public void onFailure(Call<InventoryRecordAPI> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }

    }

}
