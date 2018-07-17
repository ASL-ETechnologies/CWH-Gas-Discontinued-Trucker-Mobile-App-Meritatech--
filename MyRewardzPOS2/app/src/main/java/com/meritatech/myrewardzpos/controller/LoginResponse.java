package com.meritatech.myrewardzpos.controller;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 12/8/2017.
 */

public class LoginResponse {
    @SerializedName("newtoken")
    public String NewToken;

    public String getNewToken() {
        return NewToken;
    }

    @SerializedName("salesmanId")
    public String SalesmanId;

    public String getSalesmanId() {
        return SalesmanId;
    }

    @SerializedName("lName")
    public String LNameId;

    public String getlName() {
        return LNameId;
    }

    @SerializedName("fName")
    public String FName;

    public String getfName() {
        return FName;
    }

    @SerializedName("parStoreId")
    public String ParStoreId;

    public String getparStoreId() {
        return ParStoreId;
    }

    @SerializedName("storeId")
    public String StoreId;

    public String getStoreId() {
        return StoreId;
    }

    @SerializedName("PIN")
    public String PIN;

    public String getPIN() {
        return PIN;
    }

    @SerializedName("realmId")
    public String RealmId;

    public String getrealmId() {
        return RealmId;
    }

    @SerializedName("storeName")
    public String StoreName;

    public String getStoreName() {
        return StoreName;
    }

    @SerializedName("allowPriceChange")
    public String allowPriceChange;

    public String getallowPriceChange() {
        return allowPriceChange;
    }

    @SerializedName("quantityType")
    public String quantityType;

    public String getquantityType() {
        return quantityType;
    }

    @SerializedName("roundSalesUpFactor")
    public String roundSalesUpFactor;

    public String getroundSalesUpFactor() {
        return roundSalesUpFactor;
    }


    @SerializedName("enableGPS")
    public String enableGPS;

    public String getenableGPS() {
        return enableGPS;
    }

    @SerializedName("taxIncluded")
    public String taxIncluded;

    public String gettaxIncluded() {
        return taxIncluded;
    }


    @SerializedName("storeAddress")
    public String StoreAddress;

    public String getStoreAddress() {
        return StoreAddress;
    }

    @SerializedName("storePhone")
    public String StorePhone;

    public String getstorePhone() {
        return StorePhone;
    }

    @SerializedName("storeEmail")
    public String StoreEmail;

    public String getstoreEmail() {
        return StoreEmail;
    }

    @SerializedName("pointsPerDollar")
    public String PointsPerDollar;

    public String getPointsPerDollar() {
        return PointsPerDollar;
    }

    @SerializedName("invCancelAgeDays")
    public String InvCancelAgeDays;

    public String getinvCancelAgeDays() {
        return InvCancelAgeDays;
    }

    @SerializedName("invSoPurgeAgeDays")
    public String InvSoPurgeAgeDays;

    public String getinvSoPurgeAgeDays() {
        return InvSoPurgeAgeDays;
    }

    @SerializedName("invDwnldTime")
    public String InvDwnldTime;

    public String getinvDwnldTime() {
        return InvDwnldTime;
    }

    @SerializedName("custDwnldTime")
    public String CustDwnldTime;

    public String getcustDwnldTime() {
        return CustDwnldTime;
    }

    @SerializedName("soDwnldPeriodMins")
    public String SoDwnldPeriodMins;

    public String getsoDwnldPeriodMins() {
        return SoDwnldPeriodMins;
    }

    @SerializedName("soAgingLimitHours")
    public String SoAgingLimitHours;

    @SerializedName("killApplication")
    public int killApplication;

    public String getsoAgingLimitHours() {
        return SoAgingLimitHours;
    }

    @SerializedName("invoiceFooterMessage")
    public String invoiceFooterMessage;

    public String getinvoiceFooterMessage() {
        return invoiceFooterMessage;
    }

    @SerializedName("salesTaxAbbr")
    public String salesTaxAbbr;

    public String getsalesTaxAbbr() {
        return salesTaxAbbr;
    }

    @SerializedName("sTxnDwnldPeriodMins")
    public String STxnDwnldPeriodMins;

    public String getsTxnDwnldPeriodMins() {
        return STxnDwnldPeriodMins;
    }

    @SerializedName("operationStartTime")
    public String OperationStartTime;

    public String getoperationStartTime() {
        return OperationStartTime;
    }

    @SerializedName("operationEndTime")
    public String OperationEndTime;

    public String getoperationEndTime() {
        return OperationEndTime;
    }

    @SerializedName("highCustomerLimit")
    public String highCustomerLimit;

    public String gethighCustomerLimit() {
        return highCustomerLimit;
    }

    @SerializedName("custRefreshPeriodMins")
    public String custRefreshPeriodMins;

    public String getcustRefreshPeriodMins() {
        return custRefreshPeriodMins;
    }

    @SerializedName("writeSalesDirectToApi")
    public String writeSalesDirectToApi;

    public String getwriteSalesDirectToApi() {
        return writeSalesDirectToApi;
    }
}
