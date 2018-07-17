package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.database.ColumnIndex;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.database.Unique;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.UUID;

/**
 * Created by Waithera on 12/1/2017.
 */
@PersistAnnotation
public class UserRecord extends DatabaseObject {

    @Unique
    @SerializedName("salesmanId")
    public String salesmanId;

    public String getsalesmanId() {
        return salesmanId;
    }

    @SerializedName("parStoreId")
    public String parStoreId;

    public String getparStoreId() {
        return parStoreId;
    }

    @SerializedName("storeId")
    public String storeId;

    public String getstoreId() {
        return storeId;
    }


    @SerializedName("PIN")
    public String PIN;

    public String getPIN() {
        return PIN;
    }

    @SerializedName("realmId")
    public String realmId;

    public String getrealmId() {
        return realmId;
    }

    @SerializedName("userPassword")
    public String userPassword;

    public String getuserPassword() {
        return userPassword;
    }


    @SerializedName("storeName")
    public String storeName;

    public String getstoreName() {
        return storeName;
    }

    @SerializedName("storeAddress")
    public String storeAddress;

    public String getstoreAddress() {
        return storeAddress;
    }

    @SerializedName("storePhone")
    public String storePhone;

    public String getstorePhone() {
        return storePhone;
    }

    @SerializedName("storeEmail")
    public String storeEmail;

    public String getstoreEmail() {
        return storeEmail;
    }

    @SerializedName("pointsPerDollar")
    public Integer pointsPerDollar;

    public Integer getpointsPerDollar() {
        return pointsPerDollar;
    }

    @SerializedName("invCancelAgeDays")
    public String invCancelAgeDays;

    public String getinvCancelAgeDays() {
        return invCancelAgeDays;
    }

    @SerializedName("invSoPurgeAgeDays")
    public String invSoPurgeAgeDays;

    public String getinvSoPurgeAgeDays() {
        return invSoPurgeAgeDays;
    }

    @SerializedName("invDwnldTime")
    public String invDwnldTime;

    public String getinvDwnldTime() {
        return invDwnldTime;
    }

    @SerializedName("custDwnldTime")
    public String custDwnldTime;

    public String getcustDwnldTime() {
        return custDwnldTime;
    }

    @SerializedName("parentStoreId")
    public String parentStoreId;

    public String getparentStoreId() {
        return parentStoreId;
    }

    @SerializedName("soDwnldPeriodMins")
    public String soDwnldPeriodMins;

    public String getsoDwnldPeriodMins() {
        return soDwnldPeriodMins;
    }

    @SerializedName("soAgingLimitHours")
    public String soAgingLimitHours;

    public String getsoAgingLimitHours() {
        return soAgingLimitHours;
    }

    @SerializedName("sTxnDwnldPeriodMins")
    public String sTxnDwnldPeriodMins;

    public String getsTxnDwnldPeriodMins() {
        return sTxnDwnldPeriodMins;
    }

    @SerializedName("offlineModeLimitHours")
    public String offlineModeLimitHours;

    public String getofflineModeLimitHours() {
        return offlineModeLimitHours;
    }

    @SerializedName("operationStartTime")
    public String operationStartTime;

    public String getoperationStartTime() {
        return operationStartTime;
    }

    @SerializedName("operationEndTime")
    public String operationEndTime;

    public String getoperationEndTime() {
        return operationEndTime;
    }

    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}
