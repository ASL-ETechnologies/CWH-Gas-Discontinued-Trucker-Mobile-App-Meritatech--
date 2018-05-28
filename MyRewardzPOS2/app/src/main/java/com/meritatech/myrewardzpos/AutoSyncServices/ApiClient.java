package com.meritatech.myrewardzpos.AutoSyncServices;

import com.meritatech.myrewardzpos.controller.GlobalVariables;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Waithera on 12/1/2017.
 */

public class ApiClient {
    static GlobalVariables globalVars = new GlobalVariables();

    public static final String BASE_URL = globalVars.url;

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
