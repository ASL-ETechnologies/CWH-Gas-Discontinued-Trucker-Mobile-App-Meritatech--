package com.meritatech.myrewardzpos;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.data.CustomerRecordAPI;
import com.meritatech.myrewardzpos.data.CustomerRecordFind;
import com.meritatech.myrewardzpos.data.CustomerRecordFindCany;
import com.meritatech.myrewardzpos.data.CustomerRecordListAPI;
import com.meritatech.myrewardzpos.data.CustomerRecordTimeStamp;
import com.meritatech.myrewardzpos.data.InventoryListAPI;
import com.meritatech.myrewardzpos.data.InventoryRecordAPI;
import com.meritatech.myrewardzpos.data.LoginRecordAPI;
import com.meritatech.myrewardzpos.data.RefreshTokenApi;
import com.meritatech.myrewardzpos.data.SalesOrderDataModel2;
import com.meritatech.myrewardzpos.data.SalesOrderRecordListAPI;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;
import com.meritatech.myrewardzpos.model.CustomerAvailabilityDataModel;
import com.meritatech.myrewardzpos.model.CustomerDataModel;
import com.meritatech.myrewardzpos.model.CustomerUsabilityDataModel;
import com.meritatech.myrewardzpos.model.InvoiceDataModel;
import com.meritatech.myrewardzpos.model.PosServicesInterface;
import com.meritatech.myrewardzpos.model.SalesOrderDataModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.security.AccessController.getContext;

/**
 * Created by Dennis Mwangi on 1/26/2018.
 */

public class ApiUtility implements PosServicesInterface {
    private PosServicesInterface service;

    public  ApiUtility() {
        GlobalVariables globalVars = new GlobalVariables();
        String API_BASE_URL = globalVars.url;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
        //  PosServicesInterface client = retrofit.create(PosServicesInterface.class);
        service = retrofit.create(PosServicesInterface.class);
    }



    @Override
    public Call<InventoryListAPI> inventoryList(String ParentId, String StoreId, String token, String smId) {
        return null;
    }

    @Override
    public Call<InventoryRecordAPI> inventorySingle(String ParentId, String StoreId, String ClassId, String sku, String token) {
        return service.inventorySingle(ParentId, StoreId, ClassId, sku, token);
    }

    @Override
    public Call<SalesOrderDataModel> getSalesOders(String salesManId, String token) {
        return service.getSalesOders(salesManId,token);
    }

    @Override
    public Call<CustomerDataModel> getCustomer(String rId, String token, String cId) {
        return null;
    }

    @Override
    public Call<CustomerDataModel> customerList(String rId, String token, Integer start, Integer limit) {
        return service.customerList(rId,token,start,limit);
    }


    @Override
    public Call<CustomerRecordListAPI> customerListByTimeStamp(Integer start, Integer limit, String RealmId, String token, String tStamp) {
        return null;
    }

    @Override
    public Call<CustomerRecordAPI> customerSingle(String RealmId, String CustomerId, String token) {
        return null;
    }

    @Override
    public Call<CustomerRecordTimeStamp> customerSingleByTimeStamp(String RealmId, String token, String TimeStampValue, String startingRecordPosition, String numberOfRecords) {
        return null;
    }

    @Override
    public Call<CustomerRecordFind> customerFind(String RealmId, String token, String cEmail, String cTel, String cAddr, String cName) {
        return null;
    }

    @Override
    public Call<CustomerRecordFindCany> customerFindCany(String RealmId, String token, String cAny) {
        return null;
    }

    @Override
    public Call<LoginRecordAPI> loginRequest(String SalesmanID, String Password) {
        return null;
    }

    @Override
    public Call<RefreshTokenApi> refreshToken(String token) {
        return null;
    }

    @Override
    public Call<SalesOrderRecordListAPI> salesOrderRecordList(String SalesOrderId, String token) {
        return null;
    }

    @Override
    public Call<SalesOrderDataModel2> salesOrderRecord(String salesmanID, String salesOrderId, String token) {
        return null;
    }

    @Override
    public Call<InvoiceDataModel> saveInvoice(String token, InvoiceDataObj data) {
        return null;
    }

    @Override
    public Call<CustomerAvailabilityDataModel> checkCustomerAvailability(String token, String RealmId, String cId) {
        return null;
    }

    @Override
    public Call<CustomerUsabilityDataModel> checkCustomerUsability(String token, String RealmId, String cId) {
        return null;
    }
}
