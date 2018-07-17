package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.database.ColumnIndex;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.database.Unique;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Waithera on 12/1/2017.
 */
@PersistAnnotation
public class InventoryRecord extends DatabaseObject {
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


    @SerializedName("itemNum")
    public String itemNum;

    public String getitemNum() {
        return itemNum;
    }


    @SerializedName("taxPercentage")
    public String taxPercentage;
    public String gettaxPercentage() {
        return taxPercentage;
    }


    @SerializedName("pointsValue")
    public String pointsValue;

    public String getpointsValue() {
        return pointsValue;
    }



    public void setitemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }


}
