package com.kumar.prince.foodneturationchecker.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *  Created by prince on 25/8/17.
 * */
public class FC_Product {

    private long mId;
    private boolean mParsed;
    private boolean mBookmarked;

    @SerializedName("code")
    @Expose
    private String mBarcode;
    @SerializedName("generic_name")
    @Expose
    private String mGenericName;
    @SerializedName("product_name")
    @Expose
    private String mProductName;
    @SerializedName("quantity")
    @Expose
    private String mQuantity;
    @SerializedName("nutrition_grades")
    @Expose
    private String mNutritionGrades;
    @SerializedName("categories_tags")
    @Expose
    private List<String> mParsableCategories;
    @SerializedName("brands")
    @Expose
    private String mBrands;
    @SerializedName("image_front_small_url")
    @Expose
    private String mImageFrontSmallUrl;
    @SerializedName("image_front_url")
    @Expose
    private String mImageFrontUrl;

    /**
     * No args constructor for use in serialization
     *
     */
    public FC_Product() {
    }

    public FC_Product(long mId, boolean mParsed, boolean mBookmarked) {
        this.mId = mId;
        this.mParsed = mParsed;
        this.mBookmarked = mBookmarked;
    }

    public String getmBarcode() {
        return mBarcode;
    }

    public String getmGenericName() {
        return mGenericName;
    }

    public String getmProductName() {
        return mProductName;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public String getmNutritionGrades() {
        return mNutritionGrades;
    }

    public List<String> getmParsableCategories() {
        return mParsableCategories;
    }

    public String getmBrands() {
        return mBrands;
    }

    public String getmImageFrontSmallUrl() {
        return mImageFrontSmallUrl;
    }

    public String getmImageFrontUrl() {
        return mImageFrontUrl;
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
                ", mBarcode='" + mBarcode + '\'' +
                ", mGenericName='" + mGenericName + '\'' +
                ", mProductName='" + mProductName + '\'' +
                ", mQuantity='" + mQuantity + '\'' +
                ", mNutritionGrades='" + mNutritionGrades + '\'' +
                ", mParsableCategories=" + mParsableCategories +
                ", mBrands='" + mBrands + '\'' +
                ", mImageFrontSmallUrl='" + mImageFrontSmallUrl + '\'' +
                ", mImageFrontUrl='" + mImageFrontUrl + '\'' +
                '}';
    }
}