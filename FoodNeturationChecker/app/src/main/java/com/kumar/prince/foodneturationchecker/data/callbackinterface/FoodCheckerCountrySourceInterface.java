package com.kumar.prince.foodneturationchecker.data.callbackinterface;

import android.support.annotation.NonNull;

import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerCountryCategory;


/**
 * Main entry point for accessing countryCategory data.
 */
public interface FoodCheckerCountrySourceInterface {

    void getCountryCategory(@NonNull String categoryKey, @NonNull String countryKey, @NonNull GetCountryCategoryCallback getCountryCategoryCallback);

    interface GetCountryCategoryCallback {

        void onCountryCategoryLoaded(FoodCheckerCountryCategory FCCountryCategory);

        void onError(Throwable throwable);

    }

}
