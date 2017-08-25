package com.kumar.prince.foodneturationchecker.data.callbackinterface;

import android.support.annotation.NonNull;

import com.kumar.prince.foodneturationchecker.data.model.FC_CountryCategory;


/**
 * Main entry point for accessing countryCategory data.
 */
public interface FC_CountrySourceInterface {

    interface GetCountryCategoryCallback {

        void onCountryCategoryLoaded(FC_CountryCategory FCCountryCategory);

        void onError(Throwable throwable);

    }

    void getCountryCategory(@NonNull String categoryKey, @NonNull String countryKey, @NonNull GetCountryCategoryCallback getCountryCategoryCallback);

}
