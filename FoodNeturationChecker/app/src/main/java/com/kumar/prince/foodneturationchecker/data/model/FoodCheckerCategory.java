package com.kumar.prince.foodneturationchecker.data.model;

/**
 *  Created by prince on 25/8/17.
 * */

public class FC_Category {


    private long mId;
    private String mCategoryKey;
    private String mCategoryName;

    public FC_Category(long mId, String mCategoryKey, String mCategoryName) {
        this.mId = mId;
        this.mCategoryKey = mCategoryKey;
        this.mCategoryName = mCategoryName;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmCategoryKey() {
        return mCategoryKey;
    }

    public void setmCategoryKey(String mCategoryKey) {
        this.mCategoryKey = mCategoryKey;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public void setmCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FC_Category)) return false;

        FC_Category that = (FC_Category) o;

        if (getmId() != that.getmId()) return false;
        if (!getmCategoryKey().equals(that.getmCategoryKey())) return false;
        return getmCategoryName().equals(that.getmCategoryName());
    }


    @Override
    public int hashCode() {
        int result = (int) (getmId() ^ (getmId() >>> 32));
        result = 31 * result + getmCategoryKey().hashCode();
        result = 31 * result + getmCategoryName().hashCode();
        return result;
    }


}