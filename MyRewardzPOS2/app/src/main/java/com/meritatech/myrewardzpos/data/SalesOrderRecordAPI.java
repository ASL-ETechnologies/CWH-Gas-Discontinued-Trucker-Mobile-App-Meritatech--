package com.meritatech.myrewardzpos.data;

import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.model.PosServicesInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Waithera on 12/1/2017.
 */

public class SalesOrderRecordAPI extends  APIBase<SalesOrderRecord> {

    public void SalesOrderRecordAPICall(String salesOrderId)

    {

        final MyPosBase myPosBase= new MyPosBase();


        try {

            SalesOrderRecordAPI reesponse;
            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call <SalesOrderDataModel2>call = inventoryInterface.salesOrderRecord(GlobalVariables.salesmanId,salesOrderId,GlobalVariables.token);

            call.enqueue(new Callback<SalesOrderDataModel2>() {
                @Override
                public void onResponse(Call<SalesOrderDataModel2> call, Response<SalesOrderDataModel2> response) {
                    Response<SalesOrderDataModel2> reesponse = response;
                    ArrayList<SalesRecords> salesRecords = new ArrayList<>();
                    salesRecords=reesponse.body().Data;
                    ArrayList<Inventory> inventory = new ArrayList<>();
                    ArrayList<InventoryRecord> result = new ArrayList<>();

                    if(response.body().StatusCode=="0") {


                        for (int i = 0; i < salesRecords.size(); i++) {

                            inventory=salesRecords.get(i).soDetailArr;

                            for (int j = 0; j < inventory.size(); j++)
                            {

                                String itemNo=inventory.get(j).itemNum;
                                result= myPosBase.findAllWithinViewPort(itemNo);

                            }

                            if (result.isEmpty())
                            {
                                //means we dont  have the inventory item locally.Call to update inventory
                                 InventoryListAPI inventoryListAPI = new InventoryListAPI();
                                  inventoryListAPI.InventoryListAPICall();


                            }


                        }


                     //   myPosBase.saveSalesOrder((ArrayList<SalesRecords>) reesponse.body().Data);
                    }
                }

                @Override
                public void onFailure(Call<SalesOrderDataModel2>call, Throwable t) {

                }
            });
        }catch (Exception ex)
        {
            ex.getMessage();
        }

    }

}
