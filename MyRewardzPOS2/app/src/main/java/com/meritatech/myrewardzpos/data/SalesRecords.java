package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.database.ColumnIndex;
import com.meritatech.myrewardzpos.database.Ignore;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.database.Unique;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Waithera on 12/12/2017.
 */
@PersistAnnotation
public  class SalesRecords extends DatabaseObject {
        @ColumnIndex
        @Unique
        @SerializedName("salesOrderId")
        public String salesOrderId;
        public String getsalesOrderId(){
            return  salesOrderId;
        }


        @SerializedName("orderDate")
        public String orderDate;
        public String getorderDate(){
            return  orderDate;
        }


        @SerializedName("customerId")
        public String customerId;
        public String getcutomerId(){
            return  customerId;
        }


        @SerializedName("fName")
        public String fName;
        public String getfName(){
            return  fName;
        }


        @SerializedName("lName")
        public String lName;
        public String getlName(){
            return  lName;
        }

        @SerializedName("address")
        public String address;
        public String getaddress(){
            return  address;
        }

        @SerializedName("lati")
        public String lati;
        public String getlati(){
            return  lati;
        }

        @SerializedName("longi")
        public String longi;
        public String getlongi(){
            return  longi;
        }

        @SerializedName("comment")
        public String comment;
        public String getcomment(){
            return  comment;
        }

    @SerializedName("phone")
    public String phone;
    public String getphone(){
        return  phone;
    }

        @SerializedName("status")
        public String status;
        public String getstatus(){
            return  status;
        }
    @Ignore
    @SerializedName("soDetailArr")
    public ArrayList<Inventory> soDetailArr;
    public ArrayList<Inventory>getsoDetailArr(){
        return soDetailArr;
    }


    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}





