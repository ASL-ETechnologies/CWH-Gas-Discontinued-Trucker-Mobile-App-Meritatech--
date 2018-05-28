package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.database.ColumnIndex;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.database.Unique;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.UUID;

/**
 * Created by Waithera on 12/1/2017.
 */
@PersistAnnotation
public class CustomerAvailabilityRecord extends DatabaseObject {
    public CustomerAvailabilityRecord(){

    }

    @Unique
    @SerializedName("isIdAvailalble")
    public String isIdAvailalble;
    public String getisIdAvailalble(){
        return  isIdAvailalble;
    }

    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}
