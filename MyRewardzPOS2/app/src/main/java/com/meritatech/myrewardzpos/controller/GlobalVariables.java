/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meritatech.myrewardzpos.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meritatech.myrewardzpos.enums.CustomerType;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.lang.reflect.*;
/**
 * @author Waithera
 */
public class GlobalVariables {

    public static String ParentId;
    public static String realmId;
    public static String storeId;
    public static Integer pointsPerDollar;

    //have this here for testing only
    public  static int killApplication;
    public static String invoiceFooterMessage;
    public static String token;
    public static String tStamp;
    public static String soAgingLimitHours;
    public static String userPassword;
    public String url = "https://api.my-points-rewardz.com/";
    public static String salesmanId;
    public static String invDwnldTime;
    public static String custDwnldTime;
    public static String custRefreshPeriodMins;
    public static String soDwnldPeriodMins;
    public static String sTxnDwnldPeriodMins;
    public static String invCancelAgeDays;
    public static String invSoPurgeAgeDays;
    public static String salesTaxAbbreviation;
    public static String allowPriceChange;
    public static String roundSalesUpFactor;
    public static String enableGPS;
    public static String taxIncluded;
    public static String highCustomerLimit;

    public static String quantityType;
    public static String StoreName;
    public  static boolean IsSalesOrder = false;
    public  static boolean IsCustomerLocation = false;
    public static CustomerType customerType = null;
    public  static int  CsRecCounter = 0;
    static final java.text.SimpleDateFormat sdf =
            new java.text.SimpleDateFormat("yyyyMMddHHmmss");
    public static String Dburl = "jdbc:sqlite:D:/sqlite/db/TestApiDb.db";
    public static Locale getCurrentLocale(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return c.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return c.getResources().getConfiguration().locale;
        }}

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }



}
