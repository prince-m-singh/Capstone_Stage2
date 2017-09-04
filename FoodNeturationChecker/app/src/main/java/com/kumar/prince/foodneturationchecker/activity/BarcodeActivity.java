package com.kumar.prince.foodneturationchecker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.kumar.prince.foodneturationchecker.Barcode.ScanBarcodeActivity;
import com.kumar.prince.foodneturationchecker.Error.FC_ServerUnreachableException;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.communication.FC_OpenFoodFactsAPIClient;
import com.kumar.prince.foodneturationchecker.communication.FC_ProductBarcode;
import com.kumar.prince.foodneturationchecker.communication.FC_Search;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FC_EventSourceInterface;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FC_ProductSourceInterface;
import com.kumar.prince.foodneturationchecker.data.local.FC_EventDataSource;
import com.kumar.prince.foodneturationchecker.data.model.FC_Event;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.kumar.prince.foodneturationchecker.Barcode.ScanBarcodeActivity.AUTOFOCUS_ENABLE;
import static com.kumar.prince.foodneturationchecker.Barcode.ScanBarcodeActivity.GETRESULT;

/**
 * Created by prince on 2/9/17.
 */

public class BarcodeActivity extends AppCompatActivity implements FC_ProductSourceInterface{
    FC_Event fc_event;
    TextView tvResult;
    Button btnScanner,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        tvResult = (TextView) findViewById(R.id.tvResult);
        btnScanner = (Button) findViewById(R.id.btnScanner);
        btn2=(Button) findViewById(R.id.button2);

        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanBarcode();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkEvents();
            }
        });
    }


    private void scanBarcode(){
        int barCodeRequestCode = 1000;
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        intent.putExtra(AUTOFOCUS_ENABLE,true);
        startActivityForResult(intent,barCodeRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1000){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data!=null){
                    Barcode barcode = data.getParcelableExtra(GETRESULT);
                    tvResult.setText(barcode.displayValue);
                    //getProduct("0016000264601", new GetProductCallback()
                     getProduct(barcode.displayValue, new GetProductCallback(){
                        @Override
                        public void onProductLoaded(FC_Product FCProduct) {
                            tvResult.append(FCProduct.toString());
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            tvResult.append(throwable.getMessage());
                        }
                    });
                }
            }
        }



        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void getProduct(@NonNull String barcode, @NonNull GetProductCallback getProductCallback) {
        FC_OpenFoodFactsAPIClient FCOpenFoodFactsAPIClient = new FC_OpenFoodFactsAPIClient(FC_OpenFoodFactsAPIClient.ENDPOINT_BARCODE);
        Call<FC_ProductBarcode> call = FCOpenFoodFactsAPIClient.getProduct(barcode);

        call.enqueue(new Callback<FC_ProductBarcode>() {
            @Override
            public void onResponse(Call<FC_ProductBarcode> call, Response<FC_ProductBarcode> response) {

                FC_ProductBarcode fc_productBarcode = response.body();
                Timber.d(response.message()+" "+fc_productBarcode.toString());
                FC_Product fc_product=fc_productBarcode.getFCProduct();

                Timber.d(response.message()+" "+fc_product.toString());
               /* *//*Get ProductS*//*
                getProducts( fc_product.getmParsableCategories().get(0),fc_product.getmNutritionGrades(), new GetProductsCallback() {
                    @Override
                    public void onProductsLoaded(List<FC_Product> FCProducts) {
                        Timber.d(FCProducts.toString());
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
               */
                fc_event=new FC_Event(barcode,"Found");
                addEvenInDb(fc_event);
                getProductCallback.onProductLoaded(fc_product);

            }

            @Override
            public void onFailure(Call<FC_ProductBarcode> call, Throwable t) {
                FC_ServerUnreachableException e = new FC_ServerUnreachableException();
                Timber.d(e.getMessage());
                fc_event=new FC_Event(barcode,"NotFound");
                getProductCallback.onError(e);


            }
        });
    }

    @Override
    public void getProducts(@NonNull String categoryKey, @NonNull String nutritionGradeValue, @NonNull GetProductsCallback getProductsCallback) {
        Timber.d(categoryKey+" "+ nutritionGradeValue);
        FC_OpenFoodFactsAPIClient FCOpenFoodFactsAPIClient = new FC_OpenFoodFactsAPIClient(FC_OpenFoodFactsAPIClient.ENDPOINT_SEARCH);
        Call<FC_Search> call = FCOpenFoodFactsAPIClient.getProducts(categoryKey,nutritionGradeValue);

        call.enqueue(new Callback<FC_Search>() {
            @Override
            public void onResponse(Call<FC_Search> call, Response<FC_Search> response) {
                FC_Search fc_search=response.body();
                Timber.d(fc_search.toString()+fc_search.getFCProducts());
                getProductsCallback.onProductsLoaded(fc_search.getFCProducts());
            }

            @Override
            public void onFailure(Call<FC_Search> call, Throwable t) {
                FC_ServerUnreachableException e = new FC_ServerUnreachableException();
                Timber.d(e.getMessage());
                //fc_event=new FC_Event(barcode,"NotFound");
                getProductsCallback.onError(e);

            }
        });

    }

    private void testData(){
        getProduct("0016000264601", new GetProductCallback() {
            @Override
            public void onProductLoaded(FC_Product FCProduct) {
                tvResult.append(FCProduct.toString());
            }

            @Override
            public void onError(Throwable throwable) {
                tvResult.append(throwable.getMessage());
            }
        });
    }

    private void addEvenInDb(FC_Event fc_event){
        FC_EventDataSource fc_eventDataSource=FC_EventDataSource.getInstance(getContentResolver());
        fc_eventDataSource.saveEvent(fc_event, new FC_EventSourceInterface.Local.SaveEventCallback() {
            @Override
            public void onEventSaved() {
                Timber.d("DataSaved");
            }

            @Override
            public void onError() {
                Timber.d("Error");
            }
        });

    }
    private void checkEvents(){
        FC_EventDataSource fc_eventDataSource=FC_EventDataSource.getInstance(getContentResolver());
        fc_eventDataSource.saveEvent(fc_event, new FC_EventSourceInterface.Local.SaveEventCallback() {
            @Override
            public void onEventSaved() {
                Timber.d("DataSaved");
            }

            @Override
            public void onError() {
                Timber.d("Error");
            }
        });
    }
}
