package com.meritatech.myrewardzpos.model;

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
import com.meritatech.myrewardzpos.data.SalesOrderRecord;
import com.meritatech.myrewardzpos.data.SalesOrderRecordAPI;
import com.meritatech.myrewardzpos.data.SalesOrderRecordListAPI;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by user on 11/27/2017.
 */

public interface PosServicesInterface {

    @GET("inventory/read.php")
    Call<InventoryListAPI> inventoryList(
            @Query("pId") String ParentId,
            @Query("sId") String StoreId,
            @Query("token") String token,
            @Query("smId") String smId
    );

    @GET("inventory/read_one.php")
    Call<InventoryRecordAPI> inventorySingle(
            @Query("pId") String ParentId,
            @Query("sId") String StoreId,
            @Query("cId") String ClassId,
            @Query("sku") String sku,
            @Query("token") String token
    );

    @GET("salesOrders/read.php")
    Call<SalesOrderDataModel> getSalesOders(
            @Query("smId") String salesManId,
            @Query("token") String token
    );

    @GET("customers/read_one.php")
    Call<CustomerDataModel> getCustomer(
            @Query("rId") String rId,
            @Query("token") String token,
            @Query("cId") String cId
    );

    @GET("customers/read.php")
    Call<CustomerDataModel> customerList(
            @Query("rId") String rId,
            @Query("token") String token,
            @Query("start") Integer start,
            @Query("limit") Integer limit
    );

    @GET("customers/readByStamp.php")
    Call<CustomerRecordListAPI> customerListByTimeStamp(
            @Query("start") Integer start,
            @Query("limit") Integer limit,
            @Query("rId") String RealmId,
            @Query("token") String token,
            @Query("tStamp") String tStamp
    );

    @GET("customers/read_one.php")
    Call<CustomerRecordAPI> customerSingle(
            @Query("rId") String RealmId,
            @Query("cId") String CustomerId,
            @Query("token") String token
    );


    @GET("customers/readByStamp.php")
    Call<CustomerRecordTimeStamp> customerSingleByTimeStamp(
            @Query("rId") String RealmId,
            @Query("token") String token,
            @Query("tStamp") String TimeStampValue,
            @Query("start") String startingRecordPosition,
            @Query("limit") String numberOfRecords
    );


    @GET("customers/find.php")
    Call<CustomerRecordFind> customerFind(
            @Query("rId") String RealmId,
            @Query("token") String token,
            @Query("cEmail") String cEmail,
            @Query("cTel") String cTel,
            @Query("cAddr") String cAddr,
            @Query("cName") String cName
    );


    @GET("customers/find.php")
    Call<CustomerRecordFindCany> customerFindCany(
            @Query("rId") String RealmId,
            @Query("token") String token,
            @Query("cAny") String cAny

    );


    @GET("loginInterface.php")
    Call<LoginRecordAPI> loginRequest(
            @Query("smId") String SalesmanID,
            @Query("pwd") String Password
    );

    @GET("refresh.php")
    Call<RefreshTokenApi> refreshToken(
            @Query("token") String token

    );


    @GET("salesOrders/read.php")
    Call<SalesOrderRecordListAPI> salesOrderRecordList(
            @Query("smId") String SalesOrderId,
            @Query("token") String token


    );

    @GET("salesOrders/read_one.php")
    Call<SalesOrderDataModel2> salesOrderRecord(
            @Query("smId") String salesmanID,
            @Query("soId") String salesOrderId,
            @Query("token") String token

    );


    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("/salesTrxn/write.php")
    Call<InvoiceDataModel> saveInvoice(
            @Query("token") String token,
            @Body InvoiceDataObj data
    );

    @GET("customers/isIdAvailable.php")
    Call<CustomerAvailabilityDataModel> checkCustomerAvailability(
            @Query("token") String token,
            @Query("rId") String RealmId,
            @Query("cId") String cId
    );

    @GET("customers/isIdUsable.php")
    Call<CustomerUsabilityDataModel> checkCustomerUsability(
            @Query("token") String token,
            @Query("rId") String RealmId,
            @Query("cId") String cId
    );
}
