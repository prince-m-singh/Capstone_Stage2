package com.kumar.prince.foodneturationchecker.MVP.model;

import com.kumar.prince.foodneturationchecker.Error.FC_ServerUnreachableException;
import com.kumar.prince.foodneturationchecker.communication.FC_OpenFoodFactsAPIClient;
import com.kumar.prince.foodneturationchecker.communication.FC_Search;

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

    public interface IAllProductResponce{
        void getResponce(FC_Search fc_search);
        void getError(FC_ServerUnreachableException e);
    }

}
