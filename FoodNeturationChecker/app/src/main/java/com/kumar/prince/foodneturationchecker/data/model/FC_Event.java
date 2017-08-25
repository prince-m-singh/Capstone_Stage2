package com.kumar.prince.foodneturationchecker.data.model;

import android.support.annotation.NonNull;

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
    public long getmId() {
        return mId;
    }

    public void setmId(@NonNull long mId) {
        this.mId = mId;
    }

    public long getmTimestamp() {
        return mTimestamp;
    }

    public void setmTimestamp(long mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public String getmBarcode() {
        return mBarcode;
    }

    public void setmBarcode(String mBarcode) {
        this.mBarcode = mBarcode;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public FC_Event(@NonNull long mId, long mTimestamp, String mBarcode, String mStatus) {
        this.mId = mId;
        this.mTimestamp = mTimestamp;
        this.mBarcode = mBarcode;
        this.mStatus = mStatus;
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