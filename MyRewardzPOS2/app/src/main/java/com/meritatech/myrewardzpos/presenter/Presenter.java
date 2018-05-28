package com.meritatech.myrewardzpos.presenter;

import android.content.Context;
import android.widget.Toast;

import com.meritatech.myrewardzpos.Utilities;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.controller.SalesOrder;
import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.data.InventoryRecord;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.data.SalesOrderInventoryRecord;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;
import com.meritatech.myrewardzpos.model.CustomerDataModel;
import com.meritatech.myrewardzpos.model.PosServicesInterface;
import com.meritatech.myrewardzpos.model.SalesOrderDataModel;
import com.meritatech.myrewardzpos.view.CustomerView;
import com.meritatech.myrewardzpos.view.InventoryView;
import com.meritatech.myrewardzpos.view.SalesOrderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dennis Njagi on 1/26/2018.
 */

public class Presenter {
    MyPosBase myPosBase = new MyPosBase();
    private CustomerView cview;
    private InventoryView iview;
    private SalesOrderView soview;

    private PosServicesInterface interactor;

    public Presenter(PosServicesInterface interactor) {
        this.interactor = interactor;
    }

    public void bind(CustomerView view) {
        this.cview = view;
    }
    public void bind(SalesOrderView view) {
        this.soview = view;
    }

    public void bind(InventoryView view) {
        this.iview = view;
    }

    public void unbind() {
        cview = null;
        soview = null;
        iview = null;
    }


    public void getCustomers(String rId, String token, Integer start, Integer limit) {
        interactor.customerList(rId, token, start, limit).enqueue(new Callback<CustomerDataModel>() {
            @Override
            public void onResponse(Call<CustomerDataModel> call, Response<CustomerDataModel> response) {
                try {
                    if (response.body().dataObj != null && response.body().dataObj.Data != null) {
                        ArrayList<CustomerRecord> newdata = response.body().dataObj.Data;
                        if (cview != null)
                            cview.updateUi(newdata);
                    }
                } catch (Exception ex) {
                    Utilities.LogException(ex);
                }
            }

            @Override
            public void onFailure(Call<CustomerDataModel> call, Throwable t) {
                try {
                } catch (Exception ex) {
                    Utilities.LogException(ex);
                }

            }
        });
    }

    public void getSalesOrder(String salesmanId, String token) {
        ArrayList<SalesOrderRecord> dt = SalesOrder.findAllRecords(SalesOrderRecord.class);
        if (dt != null && dt.size() < 0) {
            interactor.getSalesOders(salesmanId, token).enqueue(new Callback<SalesOrderDataModel>() {
                @Override
                public void onResponse(Call<SalesOrderDataModel> call, Response<SalesOrderDataModel> response) {
                    try {
                        if (response.body().salesOrderDataObj != null && response.body().salesOrderDataObj.Data != null) {
                            ArrayList<SalesOrderRecord> newdata = response.body().salesOrderDataObj.Data;
                            if (soview != null)
                                soview.updateUi(newdata);
                        }
                    } catch (Exception ex) {
                        Utilities.LogException(ex);
                    }
                }

                @Override
                public void onFailure(Call<SalesOrderDataModel> call, Throwable t) {
                    try {

                    } catch (Exception ex) {
                        Utilities.LogException(ex);
                    }

                }
            });
        } else {
            for (int i = 0; i < dt.size(); i++) {
                ArrayList<SalesOrderInventoryRecord> records = myPosBase.GetSalesOrderInventoryRecords(dt.get(i).salesOrderId);
                if (records != null && records.size() > 0) {
                    for (int e = 0; e < records.size(); e++) {
                        Inventory inventory = new Inventory();
                        inventory.description = records.get(e).description;
                        inventory.itemNum = records.get(e).itemNumber;
                        inventory.qty = records.get(e).qty;
                        inventory.priceValue = records.get(e).dollarValue;
                        if (dt.get(i).SalesDetails == null) {
                            dt.get(i).SalesDetails = new ArrayList<Inventory>();
                        }
                        dt.get(i).SalesDetails.add(inventory);
                    }
                }
            }
            if (soview != null)
                soview.updateUi(dt);
        }
    }

    public void getInventory ()
    {
        List<InventoryRecord> listOfInventory = InventoryRecord.findAllRecords(InventoryRecord.class);
        ArrayList<Inventory> inventoryList = new ArrayList<>();
        if (listOfInventory!=null) {
            for (int i = 0; i <= listOfInventory.size()-1; i++) {
                String lname = "";
                String fname = "";
                Inventory inv = new Inventory();
                inv.description = listOfInventory.get(i).description;
                inv.itemNum = listOfInventory.get(i).itemNum;
                inv.sellingPrice=listOfInventory.get(i).sellingPrice;
                inv.taxPercentage =listOfInventory.get(i).taxPercentage;

                inventoryList.add(inv);
            }
            iview.updateUi(inventoryList);
        }
    }

}
