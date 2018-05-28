package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.UUID;

/**
 * Created by Waithera on 12/1/2017.
 */
@PersistAnnotation
public class SalesInvoiceInventoryRecord extends DatabaseObject {
    public SalesInvoiceInventoryRecord(){

    }



    @SerializedName("salesOrderID")
    public String salesOrderID;
    public String getsalesOrderID(){
        return  salesOrderID;
    }
    @SerializedName("itemNumber")
    public String itemNumber;
    public String getfName(){
        return itemNumber;
    }

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

    @SerializedName("customerId")
    public String customerId;

    public String getcustomerId() {
        return customerId;
    }


    public void setsalesOrderID(String salesOrderID) {
         this.salesOrderID = salesOrderID;
    }
    public void setitemNumber(String itemNumber) {
         this.itemNumber = itemNumber;
    }

    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}
