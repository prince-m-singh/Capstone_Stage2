package com.kumar.prince.foodneturationchecker.data.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.kumar.prince.foodneturationchecker.data.local.FoodCheckerEventContract;

import java.util.Random;

/**
 *  Created by prince on 25/8/17.
 * */

public class FoodCheckerEvent {

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

    public FoodCheckerEvent(@NonNull long mId, long mTimestamp, String mBarcode, String mStatus) {
        this.mId = mId;
        this.mTimestamp = mTimestamp;
        this.mBarcode = mBarcode;
        this.mStatus = mStatus;
    }

    public FoodCheckerEvent(long mTimestamp, String mBarcode, String mStatus) {
        this.mTimestamp = mTimestamp;
        this.mBarcode = mBarcode;
        this.mStatus = mStatus;
    }

    public FoodCheckerEvent(String barcode, String status) {
        Long x = 1234567L;
        Long y = 23456789L;
        Random r = new Random();
        this.mId = x + ((long)(r.nextDouble()*(y-x)));
        this.mTimestamp = System.currentTimeMillis()/1000;
        this.mBarcode = barcode;
        this.mStatus = status;
    }

    /**
     * Use this constructor to return a FoodCheckerEvent from a Cursor
     *
     * @return
     */
    public static FoodCheckerEvent from(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(
                FoodCheckerEventContract.EventEntry._ID));
        long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(
                FoodCheckerEventContract.EventEntry.COLUMN_NAME_TIMESTAMP));
        String barcode = cursor.getString(cursor.getColumnIndexOrThrow(
                FoodCheckerEventContract.EventEntry.COLUMN_NAME_BARCODE));
        String status = cursor.getString(cursor.getColumnIndexOrThrow(
                FoodCheckerEventContract.EventEntry.COLUMN_NAME_STATUS));
        return new FoodCheckerEvent(id, timestamp, barcode, status);
    }


    public static FoodCheckerEvent from(ContentValues values) {
        long id = values.getAsLong(FoodCheckerEventContract.EventEntry._ID);
        long timestamp = values.getAsLong(FoodCheckerEventContract.EventEntry.COLUMN_NAME_TIMESTAMP);
        String barcode = values.getAsString(FoodCheckerEventContract.EventEntry.COLUMN_NAME_BARCODE);
        String status = values.getAsString(FoodCheckerEventContract.EventEntry.COLUMN_NAME_STATUS);
        return new FoodCheckerEvent(id, timestamp, barcode, status);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodCheckerEvent foodChecker_event = (FoodCheckerEvent) o;

        if (mId != foodChecker_event.mId) return false;
        if (mTimestamp != foodChecker_event.mTimestamp) return false;
        if (mBarcode != null ? !mBarcode.equals(foodChecker_event.mBarcode) : foodChecker_event.mBarcode != null)
            return false;
        return mStatus != null ? mStatus.equals(foodChecker_event.mStatus) : foodChecker_event.mStatus == null;
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
        return "FoodCheckerEvent{" +
                "mId=" + mId +
                ", mTimestamp=" + mTimestamp +
                ", mBarcode='" + mBarcode + '\'' +
                ", mStatus='" + mStatus + '\'' +
                '}';
    }
}