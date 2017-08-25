package com.kumar.prince.foodneturationchecker.data.callbackinterface;

import android.support.annotation.NonNull;

import com.kumar.prince.foodneturationchecker.data.model.FC_Category;


/**
 * Main entry point for accessing category data.
 */
public interface FC_CategorySourceInterface {

    interface GetCategoryCallback {

        void onCategoryLoaded(FC_Category FCCategory);

        void onError(Throwable throwable);

    }

    void getCategory(@NonNull String categoryKey, @NonNull GetCategoryCallback getCategoryCallback);

}
