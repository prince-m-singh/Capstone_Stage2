package com.kumar.prince.foodneturationchecker.MVP.model;

import com.kumar.prince.foodneturationchecker.Error.FoodCheckServerUnreachableException;
import com.kumar.prince.foodneturationchecker.Error.FoodCheckerProductnotexistexception;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerOpenFoodFactsAPIClient;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerProductBarcode;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerSearch;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerProduct;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by prince on 13/9/17.
 */

public class AllProductRequest {
    IAllProductResponce iAllProductResponce;

    public AllProductRequest(IAllProductResponce iAllProductResponce) {
        this.iAllProductResponce = iAllProductResponce;
    }

    public void getAllProducts(String categoryKey, String nutritionGradeValue) {
        FoodCheckerOpenFoodFactsAPIClient FCOpenFoodFactsAPIClient = new FoodCheckerOpenFoodFactsAPIClient(FoodCheckerOpenFoodFactsAPIClient.ENDPOINT_SEARCH);
        Call<FoodCheckerSearch> call = FCOpenFoodFactsAPIClient.getProducts(categoryKey, nutritionGradeValue);

        call.enqueue(new Callback<FoodCheckerSearch>() {
            @Override
            public void onResponse(Call<FoodCheckerSearch> call, Response<FoodCheckerSearch> response) {
                FoodCheckerSearch foodChecker_search = response.body();
                Timber.d(foodChecker_search.getFCProducts().get(0).toString());
                iAllProductResponce.getResponce(foodChecker_search);


            }

            @Override
            public void onFailure(Call<FoodCheckerSearch> call, Throwable t) {
                FoodCheckServerUnreachableException e = new FoodCheckServerUnreachableException();
                Timber.d(e.getMessage());
                iAllProductResponce.getError(e);

            }
        });
    }


    public void getProductDetailsBarCode(String barcode) {
        FoodCheckerOpenFoodFactsAPIClient FCOpenFoodFactsAPIClient = new FoodCheckerOpenFoodFactsAPIClient(FoodCheckerOpenFoodFactsAPIClient.ENDPOINT_BARCODE);
        Call<FoodCheckerProductBarcode> call = FCOpenFoodFactsAPIClient.getProduct(barcode);

        call.enqueue(new Callback<FoodCheckerProductBarcode>() {
            @Override
            public void onResponse(Call<FoodCheckerProductBarcode> call, Response<FoodCheckerProductBarcode> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    FoodCheckServerUnreachableException e = new FoodCheckServerUnreachableException();
                    Timber.w(e);
                    // getProductCallback.onError(e,barcode);
                    return;
                }
                FoodCheckerProductBarcode foodChecker_productBarcode = response.body();
                Timber.d(response.message() + " " + foodChecker_productBarcode.toString());
                if (foodChecker_productBarcode.getStatus() != 1) {
                    FoodCheckerProductnotexistexception e = new FoodCheckerProductnotexistexception();
                    Timber.w(e);
                }
                FoodCheckerProduct foodChecker_product = foodChecker_productBarcode.getFCProduct();
                iAllProductResponce.getProductDetails(foodChecker_product);
                Timber.w(response.message() + " " + foodChecker_product.toString());
            }

            @Override
            public void onFailure(Call<FoodCheckerProductBarcode> call, Throwable t) {
                FoodCheckServerUnreachableException e = new FoodCheckServerUnreachableException();
                Timber.d(e.getMessage());
                iAllProductResponce.getError(e);
                /*foodChecker_event=new FoodCheckerEvent(barcode,"NotFound");
                getProductCallback.onError(e,barcode);*/


            }
        });


    }

    public interface IAllProductResponce {
        void getResponce(FoodCheckerSearch foodChecker_search);

        void getError(FoodCheckServerUnreachableException e);

        void getProductDetails(FoodCheckerProduct foodChecker_product);
    }


}
