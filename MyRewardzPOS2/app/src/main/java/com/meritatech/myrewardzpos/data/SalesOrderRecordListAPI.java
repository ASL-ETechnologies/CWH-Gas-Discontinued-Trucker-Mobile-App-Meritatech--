package com.meritatech.myrewardzpos.data;

import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.Utilities;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.dataObj.SalesOrderDataObj;
import com.meritatech.myrewardzpos.model.PosServicesInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Waithera on 12/1/2017.
 */

public class SalesOrderRecordListAPI extends APIBase<SalesOrderDataObj> {


    public void SalesOrderRecordListAPICall()

    {
        final MyPosBase myPosBase = new MyPosBase();
        final Integer records = SalesOrderRecord.findAllRecords(SalesOrderRecord.class).size();
        try {
            SalesOrderRecordListAPI reesponse;
            PosServicesInterface inventoryInterface =
                    ApiClient.getClient().create(PosServicesInterface.class);
            Call<SalesOrderRecordListAPI> call = inventoryInterface.salesOrderRecordList(GlobalVariables.salesmanId, GlobalVariables.token);

            call.enqueue(new Callback<SalesOrderRecordListAPI>() {
                @Override
                public void onResponse(Call<SalesOrderRecordListAPI> call, Response<SalesOrderRecordListAPI> response) {
                    try {
                        Response<SalesOrderRecordListAPI> reesponse = response;
                        ArrayList<SalesOrderRecord> salesRecords = new ArrayList<>();
                        salesRecords = reesponse.body().DataObj.Data;
                        if (records != null && records == reesponse.body().DataObj.Data.size()) {
                            return;
                        } else {
                            if (response.body().StatusCode == 0) {
                                //delete existing
                                if (records != null) {
                                    SalesOrderRecord.deleteAll(SalesOrderRecord.class);
                                    SalesOrderInventoryRecord.deleteAll(SalesOrderInventoryRecord.class);
                                }
                                if (salesRecords != null) {
                                    ArrayList<SalesOrderInventoryRecord> salesOrderInventoryRecords = new ArrayList<SalesOrderInventoryRecord>();
                                    for (int i = 0; i < salesRecords.size(); i++) {
                                        for (int e = 0; e < salesRecords.get(i).SalesDetails.size(); e++) {
                                            SalesOrderInventoryRecord salesOrderInventoryRecord = new SalesOrderInventoryRecord();
                                            salesOrderInventoryRecord.itemNumber = salesRecords.get(i).SalesDetails.get(e).itemNum;
                                            salesOrderInventoryRecord.description = salesRecords.get(i).SalesDetails.get(e).description;
                                            salesOrderInventoryRecord.qty = salesRecords.get(i).SalesDetails.get(e).qty;
                                            salesOrderInventoryRecord.dollarValue = salesRecords.get(i).SalesDetails.get(e).priceValue;
                                            salesOrderInventoryRecord.pointsValue = salesRecords.get(i).SalesDetails.get(e).pointsValue;
                                            salesOrderInventoryRecord.salesOrderID = salesRecords.get(i).salesOrderId;
                                            salesOrderInventoryRecords.add(salesOrderInventoryRecord);
                                        }
                                    }
                                    myPosBase.saveSalesOrderInventory(salesOrderInventoryRecords);
                                    myPosBase.saveSalesOrder((ArrayList<SalesOrderRecord>) reesponse.body().DataObj.Data);

                                }
                            }
                        }

                    } catch (Exception ex) {
                        Utilities.LogException(ex);
                    }
                }

                @Override
                public void onFailure(Call<SalesOrderRecordListAPI> call, Throwable t) {
                    try {
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
