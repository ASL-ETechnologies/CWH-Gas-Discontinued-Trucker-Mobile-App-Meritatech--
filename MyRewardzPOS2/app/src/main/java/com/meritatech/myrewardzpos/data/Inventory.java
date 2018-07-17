package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.UUID;

/**
 * Created by Waithera on 12/18/2017.
 */
@PersistAnnotation
public class Inventory extends DatabaseObject {


    @SerializedName("taxPercentage")
    public String taxPercentage;

    public String gettaxPercentage() {
        return taxPercentage;
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

    public int TotalItems;

    public int getTotalItems() {
        return TotalItems;
    }

    @SerializedName("classId")
    public String classId;

    public String getclassId() {
        return classId;
    }

    @SerializedName("qty")
    public String qty;

    public String getqty() {
        return qty;
    }

    @SerializedName("points")
    public String points;

    public String getpoints() {
        return points;
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


    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}
