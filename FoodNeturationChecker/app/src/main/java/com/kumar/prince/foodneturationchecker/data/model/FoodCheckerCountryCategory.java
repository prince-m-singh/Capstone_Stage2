package com.kumar.prince.foodneturationchecker.data.model;



/**
 *  Created by prince on 26/8/17.
 * */

public class FC_CountryCategory {

    private long mId;
    private String mCategoryKey;
    private String mCountryKey;
    private int mSumOfProducts;

    /**
     * No args constructor for use in serialization
     *
     */
    public FC_CountryCategory() {
    }

    /**
     *
     * @param categoryKey
     * @param sumOfProducts
     * @param countryKey
     */
    public FC_CountryCategory(String categoryKey, String countryKey, int sumOfProducts) {
        this.mCategoryKey = categoryKey;
        this.mCountryKey = countryKey;
        this.mSumOfProducts = sumOfProducts;
    }

    /**
     *
     * @param id
     * @param categoryKey
     * @param sumOfProducts
     * @param countryKey
     */
    public FC_CountryCategory(long id, String categoryKey, String countryKey, int sumOfProducts) {
        this.mId = id;
        this.mCategoryKey = categoryKey;
        this.mCountryKey = countryKey;
        this.mSumOfProducts = sumOfProducts;
    }

    /**
     *
     * @return
     * The id
     */
    public long getId() {
        return mId;
    }

    /**
     *
     * @return
     * The id
     */
    public String getAsStringId() {
        return String.valueOf(mId);
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(long id) {
        this.mId = id;
    }

    public FC_CountryCategory withId(long id) {
        this.mId = id;
        return this;
    }

    /**
     *
     * @return
     * The categoryKey
     */
    public String getCategoryKey() {
        return mCategoryKey;
    }

    /**
     *
     * @param categoryKey
     * The category-key
     */
    public void setCategoryKey(String categoryKey) {
        this.mCategoryKey = categoryKey;
    }

    public FC_CountryCategory withCategoryKey(String categoryKey) {
        this.mCategoryKey = categoryKey;
        return this;
    }

    /**
     *
     * @return
     * The countryKey
     */
    public String getCountryKey() {
        return mCountryKey;
    }

    /**
     *
     * @param countryKey
     * The country-key
     */
    public void setCountryKey(String countryKey) {
        this.mCountryKey = countryKey;
    }

    public FC_CountryCategory withCountryKey(String countryKey) {
        this.mCountryKey = countryKey;
        return this;
    }

    /**
     *
     * @return
     * The sumOfProducts
     */
    public long getSumOfProducts() {
        return mSumOfProducts;
    }

    /**
     *
     * @param sumOfProducts
     * The sum-of-products
     */
    public void setSumOfProducts(int sumOfProducts) {
        this.mSumOfProducts = sumOfProducts;
    }

    public FC_CountryCategory withSumOfProducts(int sumOfProducts) {
        this.mSumOfProducts = sumOfProducts;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FC_CountryCategory)) return false;

        FC_CountryCategory that = (FC_CountryCategory) o;

        if (mId != that.mId) return false;
        if (mSumOfProducts != that.mSumOfProducts) return false;
        if (!mCategoryKey.equals(that.mCategoryKey)) return false;
        return mCountryKey.equals(that.mCountryKey);
    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + mCategoryKey.hashCode();
        result = 31 * result + mCountryKey.hashCode();
        result = 31 * result + mSumOfProducts;
        return result;
    }

    @Override
    public String toString() {
        return "FC_CountryCategory{" +
                "mId=" + mId +
                ", mCategoryKey='" + mCategoryKey + '\'' +
                ", mCountryKey='" + mCountryKey + '\'' +
                ", mSumOfProducts=" + mSumOfProducts +
                '}';
    }


}