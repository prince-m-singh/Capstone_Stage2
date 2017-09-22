package com.kumar.prince.foodneturationchecker.data.callbackinterface;

import android.support.annotation.NonNull;

import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerCategory;


/**
 * Main entry point for accessing category data.
 */
public interface FoodCheckerCategorySourceInterface {

    interface GetCategoryCallback {

        void onCategoryLoaded(FoodCheckerCategory FCCategory);

        void onError(Throwable throwable);

    }

    void getCategory(@NonNull String categoryKey, @NonNull GetCategoryCallback getCategoryCallback);

}
