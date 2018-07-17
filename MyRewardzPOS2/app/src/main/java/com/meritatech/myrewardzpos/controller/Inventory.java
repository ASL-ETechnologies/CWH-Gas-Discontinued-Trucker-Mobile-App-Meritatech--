package com.meritatech.myrewardzpos.controller;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.UUID;
@PersistAnnotation
public class Inventory extends DatabaseObject {


    @SerializedName("taxPercentage")
    public String taxPercentage;

    public String gettaxPercentage() {
        return taxPercentage;
    }

    @SerializedName("taxVal")
    public String taxVal;

    public String gettaxVal() {
        return taxVal;
    }


    @SerializedName("description")
    public String description;

    public String getdescription() {
        return description;
    }

    @SerializedName("sellingPrice")
    public String sellingPrice;

    public String getsellingPrice() {
        return sellingPrice;
    }

    @SerializedName("sku")
    public String sku;

    public String getsku() {
        return sku;
    }

    public double TotalItems;

    public double getTotalItems() {
        return TotalItems;
    }

    @SerializedName("classId")
    public String classId;

    public String getclassId() {
        return classId;
    }

    @SerializedName("quantity")
    public String qty;

    public String getqty() {
        return qty;
    }

    @SerializedName("points")
    public String points;

    public String getpoints() {
        return points;
    }


    @SerializedName("pointsValue")
    public String pointsValue;

    public String getpointsValue() {
        return pointsValue;
    }


    @SerializedName("priceSold")
    public String priceSold;

    public String getpriceSold() {
        return priceSold;
    }

    @SerializedName("itemNum")
    public String itemNum;

    public String getitemNum() {
        return itemNum;
    }

    @SerializedName("customerId")
    public String customerId;

    public String getcustomerId() {
        return customerId;
    }


    @SerializedName("LinkerOrderId")
    public String LinkerOrderId;

    public String getLinkerOrderId() {
        return LinkerOrderId;
    }

    @SerializedName("priceValue")
    public String priceValue;

    public String getpriceValue() {
        return priceValue;
    }


    @SerializedName("isEditMode")
    public Boolean isEditMode;

    public Boolean getisEditMode() {
        return isEditMode;
    }

    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}
