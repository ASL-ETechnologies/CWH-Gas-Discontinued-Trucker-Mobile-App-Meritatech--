package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 12/22/2017.
 */

public class InventotyPost {
    @SerializedName("itemNum")
    public String itemNum;
    public String getitemNum() {
        return itemNum;
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

    @SerializedName("tax")
    public String tax;
    public String gettax() {
        return tax;
    }

    @SerializedName("quantity")
    public String quantity;
    public String getquantity() {
        return quantity;
    }

}
