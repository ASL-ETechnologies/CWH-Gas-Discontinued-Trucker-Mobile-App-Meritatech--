package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Waithera on 12/1/2017.
 */

public class LoginRecord extends MyPosBase {

    @SerializedName("newtoken")
    public String newtoken;
    public String getnewtoken(){
        return  newtoken;
    }
    @SerializedName("salesmanId")
    public int salesmanId;
    public int getsalesmanId(){
        return salesmanId;
    }
    @SerializedName("lName")
    public String lName;
    public String getlName(){
        return lName;
    }
    @SerializedName("fName")
    public String fName;
    public String getfName(){
        return fName;
    }
    @SerializedName("parStoreId")
    public int parStoreId;
    public int getparStoreId(){
        return parStoreId;
    }
    @SerializedName("storeId")
    public int storeId;
    public int getstoreId(){
        return storeId;
    }
    @SerializedName("PIN")
    public int PIN;
    public int getPIN(){
        return PIN;
    }
    @SerializedName("realmId")
    public int realmId;
    public int getrealmId(){
        return realmId;
    }
    @SerializedName("storeName")
    public String storeName;
    public String getstoreName(){
        return storeName;
    }
    @SerializedName("storeAddress")
    public String storeAddress;
    public String getstoreAddressi(){
        return storeAddress;
    }
    @SerializedName("storePhone")
    public String storePhone;
    public String getstorePhone(){
        return storePhone;
    }
    @SerializedName("storeEmail")
    public String storeEmail;
    public String getstoreEmail(){
        return storeEmail;
    }
    @SerializedName("pointsPerDollar")
    public int pointsPerDollar;
    public int getpointsPerDollar(){
        return pointsPerDollar;
    }
    @SerializedName("invCancelAgeDays")
    public int invCancelAgeDays;
    public int getinvCancelAgeDays(){
        return invCancelAgeDays;
    }
    @SerializedName("invSoPurgeAgeDays")
    public int invSoPurgeAgeDays;
    public int getinvSoPurgeAgeDays(){
        return invSoPurgeAgeDays;
    }
    @SerializedName("invDwnldTime")
    public String invDwnldTime;
    public String getinvDwnldTime(){
        return invDwnldTime;
    }
    @SerializedName("custDwnldTime")
    public String custDwnldTime;
    public String getcustDwnldTime(){
        return custDwnldTime;
    }
    @SerializedName("soDwnldPeriodMins")
    public int soDwnldPeriodMins;
    public int getsoDwnldPeriodMins(){
        return soDwnldPeriodMins;
    }
    @SerializedName("soAgingLimitHours")
    public int soAgingLimitHours;
    public int getsoAgingLimitHours(){
        return soAgingLimitHours;
    }
    @SerializedName("sTxnDwnldPeriodMins")
    public int sTxnDwnldPeriodMins;
    public int getsTxnDwnldPeriodMins(){
        return sTxnDwnldPeriodMins;
    }
    @SerializedName("operationStartTime")
    public String operationStartTime;
    public String getoperationStartTime(){
        return operationStartTime;
    }
    @SerializedName("operationEndTime")
    public String operationEndTime;
    public String getoperationEndTime(){
        return operationStartTime;
    }



}
