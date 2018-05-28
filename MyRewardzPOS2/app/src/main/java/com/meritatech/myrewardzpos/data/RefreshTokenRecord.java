package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Waithera on 12/17/2017.
 */

public class RefreshTokenRecord {


    @SerializedName("newtoken")
    public String newtoken;
    public String getnewtoken(){
        return  newtoken;
    }
}
