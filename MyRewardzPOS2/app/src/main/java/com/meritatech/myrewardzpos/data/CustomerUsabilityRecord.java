package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.database.Unique;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.UUID;

/**
 * Created by Waithera on 12/1/2017.
 */
@PersistAnnotation
public class CustomerUsabilityRecord extends DatabaseObject {
    public CustomerUsabilityRecord(){

    }

    @SerializedName("fName")
    public String fName;
    public String getfName(){
        return fName;
    }
    @SerializedName("lName")
    public String lName;
    public String getlName(){
        return lName;
    }
    @SerializedName("address")
    public String address;
    public String getaddress(){
        return address;
    }
    @SerializedName("points")
    public String points;
    public String getpoints(){
        return points;
    }
    @SerializedName("phone1")
    public String phone1;
    public String getphone1(){
        return phone1;
    }
    @SerializedName("phone2")
    public String phone2;
    public String getphone2(){
        return phone2;
    }
    @SerializedName("email")
    public String email;
    public String getemail(){
        return email;
    }
    @SerializedName("longi")
    public String longi;
    public String getlongi(){
        return longi;
    }
    @SerializedName("lati")
    public String lati;
    public String getlati(){
        return lati;
    }
    @SerializedName("tStamp")
    public String tStamp;
    public String gettStamp(){
        return tStamp;
    }


    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}
