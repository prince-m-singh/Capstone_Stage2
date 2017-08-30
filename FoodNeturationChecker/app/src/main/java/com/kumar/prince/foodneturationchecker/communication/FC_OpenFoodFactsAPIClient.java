package com.kumar.prince.foodneturationchecker.communication;

import android.support.annotation.NonNull;

import com.kumar.prince.foodneturationchecker.Error.FC_ServerUnreachableException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *  Created by prince on 29/8/17.
 * */

public class FC_OpenFoodFactsAPIClient {


    public static final String ENDPOINT_BARCODE = "http://world.openfoodfacts.org/api/v0/product/";
    public static final String ENDPOINT_SEARCH = "http://world.openfoodfacts.org/cgi/";

    private final OpenFoodFactsApi mOpenFoodFactsAPI;

    public FC_OpenFoodFactsAPIClient(@NonNull String endpoint) {
        checkNotNull(endpoint);

        this.mOpenFoodFactsAPI = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build()
                .create(OpenFoodFactsApi.class);
    }

    private OkHttpClient getOkHttpClient() {
        try {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);

            return builder.build();
        } catch (Exception e) {
            throw new FC_ServerUnreachableException();
        }
    }


    public Call<FC_ProductBarcode> getProduct(@NonNull final String barcode) {
        return this.mOpenFoodFactsAPI.getProduct(barcode);
    }

    public Call<FC_Search> getProducts(@NonNull String categoryKey,
                                       @NonNull String nutritionGradeValue) {
        return this.mOpenFoodFactsAPI.getProducts(categoryKey, nutritionGradeValue);
    }

    public Call<FC_Search> getCountryCategory(@NonNull final String categoryKey, @NonNull final String countryKey) {
        return this.mOpenFoodFactsAPI.getCountryCategory(categoryKey, countryKey);
    }



    interface OpenFoodFactsApi {

        //Example : http://world.openfoodfacts.org/api/v0/product/3046920029759.json
        @GET("{barcode}.json")
        Call<FC_ProductBarcode> getProduct(
                @Path("barcode") String barcode
        );

        @GET("search.pl?action=process&tagtype_0=categories&tag_contains_0=contains&tagtype_1=nutrition_grades&tag_contains_1=contains&sort_by=unique_scans_n&page_size=20&page=1&json=1")
        Call<FC_Search> getProducts(
                @Query("tag_0") String categoryKey,
                @Query("tag_1") String nutritionGrade
        );


        //Example : http://world.openfoodfacts.org/cgi/search.pl?action=process&tagtype_0=categories&tag_contains_0=contains&tag_0=cheese&tagtype_1=countries&tag_contains_1=contains&tag_1=france&tagtype_2=nutrition_grades&tag_contains_2=does_not_contain&tag_2=unknown&sort_by=unique_scans_n&page_size=5&page=1&json=1
        @GET("search.pl?action=process&tagtype_0=categories&tag_contains_0=contains&tagtype_1=countries&tag_contains_1=contains&tagtype_2=nutrition_grades&tag_contains_2=does_not_contain&tag_2=unknown&sort_by=unique_scans_n&page_size=5&page=1&json=1")
        Call<FC_Search> getCountryCategory(
                @Query("tag_0") String categoryKey,
                @Query("tag_1") String countryKey
        );
    }


}