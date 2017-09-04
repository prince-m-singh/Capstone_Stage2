package com.kumar.prince.foodneturationchecker.data.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.kumar.prince.foodneturationchecker.data.local.FC_EventContract;

import java.util.Random;

/**
 *  Created by prince on 25/8/17.
 * */

public class FC_Event {

    @NonNull
    private long mId;
    private long mTimestamp;
    private String mBarcode;
    private String mStatus;

    @NonNull
    public long getId() {
        return mId;
    }

    public void setId(@NonNull long mId) {
        this.mId = mId;
    }
    @NonNull
    public String getAsStringId() {
        return String.valueOf(mId);
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public String getBarcode() {
        return mBarcode;
    }

    public void setBarcode(String mBarcode) {
        this.mBarcode = mBarcode;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public FC_Event(@NonNull long mId, long mTimestamp, String mBarcode, String mStatus) {
        this.mId = mId;
        this.mTimestamp = mTimestamp;
        this.mBarcode = mBarcode;
        this.mStatus = mStatus;
    }

    public FC_Event(String barcode, String status) {
        Long x = 1234567L;
        Long y = 23456789L;
        Random r = new Random();
        this.mId = x + ((long)(r.nextDouble()*(y-x)));
        this.mTimestamp = System.currentTimeMillis()/1000;
        this.mBarcode = barcode;
        this.mStatus = status;
    }

    /**
     * Use this constructor to return a FC_Event from a Cursor
     *
     * @return
     */
    public static FC_Event from(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(
                FC_EventContract.EventEntry._ID));
        long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(
                FC_EventContract.EventEntry.COLUMN_NAME_TIMESTAMP));
        String barcode = cursor.getString(cursor.getColumnIndexOrThrow(
                FC_EventContract.EventEntry.COLUMN_NAME_BARCODE));
        String status = cursor.getString(cursor.getColumnIndexOrThrow(
                FC_EventContract.EventEntry.COLUMN_NAME_STATUS));
        return new FC_Event(id, timestamp, barcode, status);
    }


    public static FC_Event from(ContentValues values) {
        long id = values.getAsLong(FC_EventContract.EventEntry._ID);
        long timestamp = values.getAsLong(FC_EventContract.EventEntry.COLUMN_NAME_TIMESTAMP);
        String barcode = values.getAsString(FC_EventContract.EventEntry.COLUMN_NAME_BARCODE);
        String status = values.getAsString(FC_EventContract.EventEntry.COLUMN_NAME_STATUS);
        return new FC_Event(id, timestamp, barcode, status);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FC_Event fc_event = (FC_Event) o;

        if (mId != fc_event.mId) return false;
        if (mTimestamp != fc_event.mTimestamp) return false;
        if (mBarcode != null ? !mBarcode.equals(fc_event.mBarcode) : fc_event.mBarcode != null)
            return false;
        return mStatus != null ? mStatus.equals(fc_event.mStatus) : fc_event.mStatus == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + (int) (mTimestamp ^ (mTimestamp >>> 32));
        result = 31 * result + (mBarcode != null ? mBarcode.hashCode() : 0);
        result = 31 * result + (mStatus != null ? mStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FC_Event{" +
                "mId=" + mId +
                ", mTimestamp=" + mTimestamp +
                ", mBarcode='" + mBarcode + '\'' +
                ", mStatus='" + mStatus + '\'' +
                '}';
    }
}