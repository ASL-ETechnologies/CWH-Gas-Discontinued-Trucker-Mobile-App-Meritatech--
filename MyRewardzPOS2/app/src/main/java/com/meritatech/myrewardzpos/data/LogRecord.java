package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;
import com.meritatech.myrewardzpos.database.PersistAnnotation;
import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.Date;
import java.util.UUID;


@PersistAnnotation
public class LogRecord extends DatabaseObject {

    @SerializedName("sent")
    public int sent;

    public int getsent() {
        return sent;
    }

    @SerializedName("ErrorMessage")
    public String ErrorMessage;
    public String getErrorMessage() {
        return ErrorMessage;
    }

    @SerializedName("EventDate")
    public Date EventDate = new Date();
    public Date getEventDate() {
        return EventDate;
    }

    @SerializedName("ErrorLevel")
    public String ErrorLevel;
    public String getErrorLevel() {
        return ErrorLevel;
    }

    @SerializedName("StackTrace")
    public String StackTrace;
    public String getStackTrace() {
        return StackTrace;
    }


    @Override
    public UUID getObjectID() {
        return null;
    }

    @Override
    public void setObjectID(UUID objectID) {

    }
}
