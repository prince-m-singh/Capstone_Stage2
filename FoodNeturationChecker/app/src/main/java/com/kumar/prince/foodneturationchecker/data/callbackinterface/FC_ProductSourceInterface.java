package com.kumar.prince.foodneturationchecker.data.callbackinterface;

import android.support.annotation.NonNull;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;

import java.util.List;

/**
 * Main entry point for accessing product data.
 */
public interface FC_ProductSourceInterface {

    interface GetProductCallback {

        void onProductLoaded(FC_Product FCProduct);

        void onError(Throwable throwable);

    }

    interface GetProductsCallback {

        void onProductsLoaded(List<FC_Product> FCProducts);

        void onError(Throwable throwable);

    }

    void getProduct(@NonNull String barcode, @NonNull GetProductCallback getProductCallback);

    void getProducts(@NonNull String categoryKey, @NonNull String nutritionGradeValue, @NonNull GetProductsCallback getProductsCallback);

}
