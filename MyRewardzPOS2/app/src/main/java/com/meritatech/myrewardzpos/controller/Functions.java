/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meritatech.myrewardzpos.controller;
import android.util.Log;

import com.meritatech.myrewardzpos.interfaces.SalesOrderInterface;

import java.text.ParseException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;


/**
 *
 * @author Waithera
 *
 * this class contains common functions to all pos Api requests
 */


public class Functions {
    GlobalVariables globalVars = new GlobalVariables();
    String output = "";

    //post and get the responce
    //function expects a route url to the actual request
    public String getPlainTextResponse() {

        try {

//            String API_BASE_URL = globalVars.url;
//
//            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//
//            Retrofit.Builder builder =
//                    new Retrofit.Builder()
//                            .baseUrl(API_BASE_URL)
//                            .addConverterFactory(
//                                    GsonConverterFactory.create()
//                            );
//
//            Retrofit retrofit =
//                    builder
//                            .client(
//                                    httpClient.build()
//                            )
//                            .build();
//
//            SalesOrderInterface client =  retrofit.create(SalesOrderInterface.class);
//            Call<ArrayList<SalesOrder>> call = client.getSalesOders(globalVars.salesmanId,"bf7a1762d11c11e7b06d525400cf57f7");
//
//            call.enqueue(new Callback<ArrayList<SalesOrder>>() {
//                @Override
//                public void onResponse(Call<ArrayList<SalesOrder>> call, Response<ArrayList<SalesOrder>> response) {
//                    Response<ArrayList<SalesOrder>> reesponse = response;
//                   Log.d(TAG, String.valueOf(response.body().get(0))) ;
//                }
//
//                @Override
//                public void onFailure(Call<ArrayList<SalesOrder>> call, Throwable t) {
//
//                }
//
//            });


            } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }
    
    ////validate idf time is in frmat (yyyyMMddHHmmss)
    
public boolean verifyDateFormat(String input) {
    
    boolean correctFormat=false;
  if (input != null) {
    try {
      java.util.Date ret = GlobalVariables.sdf.parse(input.trim());
      if (GlobalVariables.sdf.format(ret).equals(input.trim())) {
         correctFormat=true;
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
  return correctFormat;
}
   

}
