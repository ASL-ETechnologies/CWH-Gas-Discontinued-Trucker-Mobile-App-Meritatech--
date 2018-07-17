package com.meritatech.myrewardzpos.interfaces;

import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.model.CustomerDataModel;
import com.meritatech.myrewardzpos.model.DataModel;
import com.meritatech.myrewardzpos.model.LoginDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 11/27/2017.
 */

public interface InventoryInterface {

    @GET("inventory/read.php")
    Call<DataModel> inventoryList(
            @Query("pId") String ParentId,
            @Query("sId") String StoreId,
            @Query("token") String token
    );



    @GET("customers/find.php")
    Call<CustomerDataModel> findcustomer(
            @Query("rId") String rId,
            @Query("cId") String cId,
            @Query("cEmail") String cEmail,
            @Query("cTel") String cTel,
            @Query("cAddr") String cAddr,
            @Query("cName") String cName,
            @Query("start") Integer start,
            @Query("limit") Integer limit,
            @Query("token") String token

    );


}
