package com.kumar.prince.foodneturationchecker.MVP.model;

import com.kumar.prince.foodneturationchecker.Error.FC_ProductNotExistException;
import com.kumar.prince.foodneturationchecker.Error.FC_ServerUnreachableException;
import com.kumar.prince.foodneturationchecker.communication.FC_OpenFoodFactsAPIClient;
import com.kumar.prince.foodneturationchecker.communication.FC_ProductBarcode;
import com.kumar.prince.foodneturationchecker.communication.FC_Search;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;

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

    public void getAllProducts(String categoryKey, String nutritionGradeValue){
        FC_OpenFoodFactsAPIClient FCOpenFoodFactsAPIClient = new FC_OpenFoodFactsAPIClient(FC_OpenFoodFactsAPIClient.ENDPOINT_SEARCH);
        Call<FC_Search> call = FCOpenFoodFactsAPIClient.getProducts(categoryKey,nutritionGradeValue);

        call.enqueue(new Callback<FC_Search>() {
            @Override
            public void onResponse(Call<FC_Search> call, Response<FC_Search> response) {
                FC_Search fc_search = response.body();
                Timber.d(fc_search.getFCProducts().get(0).toString());
                iAllProductResponce.getResponce(fc_search);


            }

            @Override
            public void onFailure(Call<FC_Search> call, Throwable t) {
                FC_ServerUnreachableException e = new FC_ServerUnreachableException();
                Timber.d(e.getMessage());
                iAllProductResponce.getError(e);

            }
        });
    }


    public void getProductDetailsBarCode(String barcode){
        FC_OpenFoodFactsAPIClient FCOpenFoodFactsAPIClient = new FC_OpenFoodFactsAPIClient(FC_OpenFoodFactsAPIClient.ENDPOINT_BARCODE);
        Call<FC_ProductBarcode> call = FCOpenFoodFactsAPIClient.getProduct(barcode);

        call.enqueue(new Callback<FC_ProductBarcode>() {
            @Override
            public void onResponse(Call<FC_ProductBarcode> call, Response<FC_ProductBarcode> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    FC_ServerUnreachableException e = new FC_ServerUnreachableException();
                    Timber.w(e);
                    // getProductCallback.onError(e,barcode);
                    return;
                }
                FC_ProductBarcode fc_productBarcode = response.body();
                Timber.d(response.message()+" "+fc_productBarcode.toString());
                if (fc_productBarcode.getStatus() != 1) {
                    FC_ProductNotExistException e = new FC_ProductNotExistException();
                    Timber.w(e);
                }
                FC_Product fc_product=fc_productBarcode.getFCProduct();
                iAllProductResponce.getProductDetails(fc_product);
                Timber.w(response.message()+" "+fc_product.toString());
            }

            @Override
            public void onFailure(Call<FC_ProductBarcode> call, Throwable t) {
                FC_ServerUnreachableException e = new FC_ServerUnreachableException();
                Timber.d(e.getMessage());
                iAllProductResponce.getError(e);
                /*fc_event=new FC_Event(barcode,"NotFound");
                getProductCallback.onError(e,barcode);*/


            }
        });


    }

    public interface IAllProductResponce{
        void getResponce(FC_Search fc_search);
        void getError(FC_ServerUnreachableException e);
        void getProductDetails(FC_Product fc_product);
    }



}
