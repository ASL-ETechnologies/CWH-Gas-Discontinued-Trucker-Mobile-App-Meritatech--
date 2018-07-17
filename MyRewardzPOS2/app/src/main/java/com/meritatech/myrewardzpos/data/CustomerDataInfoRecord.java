package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Dennis Njagi on 1/18/2018.
 */

public class CustomerDataInfoRecord extends DatabaseObject {

    @SerializedName("entryTimestamp")
    public String entryTimestamp;

    @SerializedName("expectedtotalRecords")
    public int expectedtotalRecords;

    @SerializedName("recNumber")
    public int recNumber;

    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}
