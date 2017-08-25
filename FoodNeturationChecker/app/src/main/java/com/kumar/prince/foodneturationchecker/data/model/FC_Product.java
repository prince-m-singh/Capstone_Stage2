package com.kumar.prince.foodneturationchecker.data.model;

/**
 *  Created by prince on 25/8/17.
 * */
public class FC_Product {

    private long mId;
    private boolean mParsed;
    private boolean mBookmarked;

    public FC_Product(long mId, boolean mParsed, boolean mBookmarked) {
        this.mId = mId;
        this.mParsed = mParsed;
        this.mBookmarked = mBookmarked;
    }

    public long getmId() {
        return mId;
    }

    public boolean ismParsed() {
        return mParsed;
    }

    public boolean ismBookmarked() {
        return mBookmarked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FC_Product)) return false;

        FC_Product that = (FC_Product) o;

        if (getmId() != that.getmId()) return false;
        if (ismParsed() != that.ismParsed()) return false;
        return ismBookmarked() == that.ismBookmarked();
    }

    @Override
    public int hashCode() {
        int result = (int) (getmId() ^ (getmId() >>> 32));
        result = 31 * result + (ismParsed() ? 1 : 0);
        result = 31 * result + (ismBookmarked() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FC_Product{" +
                "mId=" + mId +
                ", mParsed=" + mParsed +
                ", mBookmarked=" + mBookmarked +
                '}';
    }
}