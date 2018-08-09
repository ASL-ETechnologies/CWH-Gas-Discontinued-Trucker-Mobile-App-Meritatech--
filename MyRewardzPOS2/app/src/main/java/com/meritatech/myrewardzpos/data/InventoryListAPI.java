package com.meritatech.myrewardzpos.data;

import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.utility.Utilities;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.dataObj.InventoryDataObj;
import com.meritatech.myrewardzpos.interfaces.PosServicesInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Waithera on 12/1/2017.
 */

public class InventoryListAPI extends APIBase<InventoryDataObj> {
    final MyPosBase myPosBase = new MyPosBase();

    public void InventoryListAPICall() {

        try {
            final ArrayList<InventoryRecord> records = InventoryRecord.findAllRecords(InventoryRecord.class);

            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<InventoryListAPI> call = inventoryInterface.inventoryList(GlobalVariables.ParentId, GlobalVariables.storeId, GlobalVariables.token, GlobalVariables.salesmanId);
            Response<InventoryListAPI> response = call.execute();
            if (response.isSuccessful()) {
                try {
                    InventoryListAPI reesponse = response.body();
                    if (records != null && records.size() < reesponse.DataObj.Data.size()) {
                        if (response.body().StatusCode == 0) {
                            InventoryRecord.deleteAll(InventoryRecord.class);
                            myPosBase.saveInventory(reesponse.DataObj.Data);
                        }
                    } else {
                        if (response.body().StatusCode == 0) {
                            InventoryRecord.deleteAll(InventoryRecord.class);
                            myPosBase.saveInventory(reesponse.DataObj.Data);
                        }
                    }
                } catch (Exception e) {
                    Utilities.LogException(e);
                }
            } else {
                Utilities.LogException(new Exception("Failure Retrieving Inventory!"));
            }


        } catch (Exception ex) {
            Utilities.LogException(ex);
        }

    }


    public void InventoryListAPICallAsync() {

        try {
            final ArrayList<InventoryRecord> records = InventoryRecord.findAllRecords(InventoryRecord.class);

            InventoryListAPI reesponse;
            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<InventoryListAPI> call = inventoryInterface.inventoryList(GlobalVariables.ParentId, GlobalVariables.storeId, GlobalVariables.token, GlobalVariables.salesmanId);

            call.enqueue(new Callback<InventoryListAPI>() {
                @Override
                public void onResponse(Call<InventoryListAPI> call, Response<InventoryListAPI> response) {
                    try {
                        InventoryListAPI reesponse = response.body();
                        if (records != null && records.size() < reesponse.DataObj.Data.size()) {
                            if (response.body().StatusCode == 0) {
                                InventoryRecord.deleteAll(InventoryRecord.class);
                                myPosBase.saveInventory(reesponse.DataObj.Data);
                            }
                        } else {
                            if (response.body().StatusCode == 0) {
                                InventoryRecord.deleteAll(InventoryRecord.class);
                                myPosBase.saveInventory(reesponse.DataObj.Data);
                            }
                        }
                    } catch (Exception e) {
                        Utilities.LogException(e);
                    }
                }

                @Override
                public void onFailure(Call<InventoryListAPI> call, Throwable t) {
                    try {
                    } catch (Exception e) {
                        Utilities.LogException(e);
                    }

                }
            });
        } catch (Exception ex) {
            Utilities.LogException(ex);
        }

    }

    public InventoryRecord getInventoryByItemNumber(String itemNumber) {
        InventoryRecord inventoryRecord = myPosBase.GetInventoryByItemNo(itemNumber);
        return inventoryRecord;
    }

    public ArrayList<SalesInvoiceInventoryRecord> getInventoryByOrderId(String salesorderid) {
        ArrayList<SalesInvoiceInventoryRecord> inventoryRecord = myPosBase.GetLocalInventoryById(salesorderid);
        return inventoryRecord;
    }


}


