package com.kumar.prince.foodneturationchecker.data.callbackinterface;

import android.support.annotation.NonNull;

import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerProduct;

import java.util.List;

/**
 * Main entry point for accessing product data.
 */
public interface FC_ProductSourceInterface {

    interface GetProductCallback {

        void onProductLoaded(FoodCheckerProduct FCProduct);

        void onError(Throwable throwable,String barCode);

    }

    interface GetProductsCallback {

        void onProductsLoaded(List<FoodCheckerProduct> FCProducts);

        void onError(Throwable throwable);

    }

    interface SaveEventCallback {
        void save();

    }

    void getProduct(@NonNull String barcode, @NonNull GetProductCallback getProductCallback);

    void getProducts(@NonNull String categoryKey, @NonNull String nutritionGradeValue, @NonNull GetProductsCallback getProductsCallback);

}
