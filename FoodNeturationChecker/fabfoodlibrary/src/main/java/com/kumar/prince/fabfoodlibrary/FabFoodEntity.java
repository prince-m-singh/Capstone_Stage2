package com.kumar.prince.fabfoodlibrary;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by prince on 6/9/17.
 */

@Entity(tableName = "FabFood",indices = {@Index(value = {"mBarcode"},unique = true)})
public class FabFoodEntity {
    @PrimaryKey
    private String mBarcode;
    private String mGenericName;
    private String mProductName;
    private String mQuantity;
    private String mNutritionGrades;
    private List<String> mParsableCategories;
    private String mBrands;
    private String mImageFrontSmallUrl;
    private String mImageFrontUrl;

    public String getMBarcode() {
        return mBarcode;
    }

    public void setMBarcode(String mBarcode) {
        this.mBarcode = mBarcode;
    }

    public String getMGenericName() {
        return mGenericName;
    }

    public void setMGenericName(String mGenericName) {
        this.mGenericName = mGenericName;
    }

    public String getMProductName() {
        return mProductName;
    }

    public void setMProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public String getMQuantity() {
        return mQuantity;
    }

    public void setMQuantity(String mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getMNutritionGrades() {
        return mNutritionGrades;
    }

    public void setMNutritionGrades(String mNutritionGrades) {
        this.mNutritionGrades = mNutritionGrades;
    }

    public List<String> getMParsableCategories() {
        return mParsableCategories;
    }

    public void setMParsableCategories(List<String> mParsableCategories) {
        this.mParsableCategories = mParsableCategories;
    }

    public String getMBrands() {
        return mBrands;
    }

    public void setMBrands(String mBrands) {
        this.mBrands = mBrands;
    }

    public String getMImageFrontSmallUrl() {
        return mImageFrontSmallUrl;
    }

    public void setMImageFrontSmallUrl(String mImageFrontSmallUrl) {
        this.mImageFrontSmallUrl = mImageFrontSmallUrl;
    }

    public String getMImageFrontUrl() {
        return mImageFrontUrl;
    }

    public void setMImageFrontUrl(String mImageFrontUrl) {
        this.mImageFrontUrl = mImageFrontUrl;
    }
}
