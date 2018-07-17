package com.meritatech.myrewardzpos.controller;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 12/4/2017.
 */

public class Customer {
    public Customer(){

    }
    private String name;
    public  Customer(String fname,String lname){
        this.name = fname;
        this.lName = lname;
    }

    public String getName(){
        return fName;
    }


    @SerializedName("customerId")
    public String customerId;

    public String getcustomerId() {
        return customerId;
    }

    @SerializedName("fName")
    public String fName;

    public String getfName() {
        return fName;
    }

    @SerializedName("lName")
    public String lName;

    public String getlName() {
        return lName;
    }

    @SerializedName("address")
    public String address;

    public String getaddress() {
        return address;
    }

    @SerializedName("points")
    public String points;

    public String getpoints() {
        return points;
    }

    @SerializedName("phone1")
    public String phone1;

    public String getphone1() {
        return phone1;
    }

    @SerializedName("phone2")
    public String phone2;

    public String getphone2() {
        return phone2;
    }

    @SerializedName("email")
    public String email;

    public String getemail() {
        return email;
    }

    @SerializedName("longi")
    public String longi;

    public String getlongi() {
        return longi;
    }

    @SerializedName("lati")
    public String lati;

    public String getlati() {
        return lati;
    }

    @SerializedName("tStamp")
    public String tStamp;

    public String gettStamp() {
        return tStamp;
    }


    public String setcustomerId(String customerId) {
        return this.customerId = customerId;
    }

    public String setfName(String fName) {
        return this.fName = fName;
    }

    public String setaddress(String address) {
        return this.address = address;
    }

    public String setpoints(String points) {
        return this.points = points;
    }

    public String setphone1(String phone1) {
        return this.phone1 = phone1;
    }

    public String setphone2(String phone2) {
        return this.phone2 = phone2;
    }

    public String setemail(String email) {
        return this.email = email;
    }

    public String setlongi(String longi) {
        return this.longi = longi;
    }

    public String setlati(String lati) {
        return this.lati = lati;
    }

    public String settStamp(String tStamp) {
        return this.tStamp = tStamp;
    }

}
