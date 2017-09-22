package com.kumar.prince.foodneturationchecker.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.kumar.prince.fabfoodlibrary.FabFoodEntity;
import com.kumar.prince.fabfoodlibrary.FabFoodIntermediateLib;
import com.kumar.prince.foodneturationchecker.Barcode.ScanBarcodeActivity;
import com.kumar.prince.foodneturationchecker.Error.FoodCheckServerUnreachableException;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerOpenFoodFactsAPIClient;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerProductBarcode;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerSearch;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FoodCheckerEventSourceInterface;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FoodCheckerProductSourceInterface;
import com.kumar.prince.foodneturationchecker.data.local.EventDataSource;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerEvent;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerProduct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class BarcodeActivity extends AppCompatActivity implements FoodCheckerProductSourceInterface {
    FoodCheckerEvent foodChecker_event;
    TextView tvResult;
    Button btnScanner,btn2;
    FabFoodIntermediateLib fabFoodIntermediateLib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        fabFoodIntermediateLib=new FabFoodIntermediateLib(this);
        fabFoodIntermediateLib.dbInitialize();
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
                    getProduct("0016000264601", new GetProductCallback(){
                     //getProduct(barcode.displayValue, new GetProductCallback(){
                        @Override
                        public void onProductLoaded(FoodCheckerProduct FCProduct) {
                            tvResult.append(FCProduct.toString());
                        }

                        @Override
                        public void onError(Throwable throwable,String data) {
                            tvResult.append(throwable.getMessage());
                        }
                    });
                }
            }
        }



        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void getProduct(@NonNull final String barcode, @NonNull final GetProductCallback getProductCallback) {
        FoodCheckerOpenFoodFactsAPIClient FCOpenFoodFactsAPIClient = new FoodCheckerOpenFoodFactsAPIClient(FoodCheckerOpenFoodFactsAPIClient.ENDPOINT_BARCODE);
        Call<FoodCheckerProductBarcode> call = FCOpenFoodFactsAPIClient.getProduct(barcode);

        call.enqueue(new Callback<FoodCheckerProductBarcode>() {
            @Override
            public void onResponse(Call<FoodCheckerProductBarcode> call, Response<FoodCheckerProductBarcode> response) {

                FoodCheckerProductBarcode foodChecker_productBarcode = response.body();
                Timber.d(response.message()+" "+ foodChecker_productBarcode.toString());
                FoodCheckerProduct foodChecker_product = foodChecker_productBarcode.getFCProduct();

                FabFoodEntity fabFoodEntity=new FabFoodEntity();
                fabFoodEntity.setMBarcode(foodChecker_product.getmBarcode());
                fabFoodEntity.setMParsableCategories(foodChecker_product.getmParsableCategories());
                fabFoodIntermediateLib.insertData(fabFoodEntity);
                Timber.d(response.message()+" "+ foodChecker_product.toString());
               /* *//*Get ProductS*//*
                getProducts( foodChecker_product.getmParsableCategories().get(0),foodChecker_product.getmNutritionGrades(), new GetProductsCallback() {
                    @Override
                    public void onProductsLoaded(List<FoodCheckerProduct> FCProducts) {
                        Timber.d(FCProducts.toString());
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
               */
                String found=getResources().getResourceName(R.string.found);
                foodChecker_event =new FoodCheckerEvent(barcode,"Found");
                addEvenInDb(foodChecker_event);
                getProductCallback.onProductLoaded(foodChecker_product);

            }

            @Override
            public void onFailure(Call<FoodCheckerProductBarcode> call, Throwable t) {
                FoodCheckServerUnreachableException e = new FoodCheckServerUnreachableException();
                Timber.d(e.getMessage());
                String notFound=getResources().getResourceName(R.string.not_found);
                foodChecker_event =new FoodCheckerEvent(barcode,notFound);
                getProductCallback.onError(e,barcode);


            }
        });
    }

    @Override
    public void getProducts(@NonNull String categoryKey, @NonNull String nutritionGradeValue, @NonNull final GetProductsCallback getProductsCallback) {
        Timber.d(categoryKey+" "+ nutritionGradeValue);
        FoodCheckerOpenFoodFactsAPIClient FCOpenFoodFactsAPIClient = new FoodCheckerOpenFoodFactsAPIClient(FoodCheckerOpenFoodFactsAPIClient.ENDPOINT_SEARCH);
        Call<FoodCheckerSearch> call = FCOpenFoodFactsAPIClient.getProducts(categoryKey,nutritionGradeValue);

        call.enqueue(new Callback<FoodCheckerSearch>() {
            @Override
            public void onResponse(Call<FoodCheckerSearch> call, Response<FoodCheckerSearch> response) {
                FoodCheckerSearch foodChecker_search =response.body();
                Timber.d(foodChecker_search.toString()+ foodChecker_search.getFCProducts());
                getProductsCallback.onProductsLoaded(foodChecker_search.getFCProducts());
            }

            @Override
            public void onFailure(Call<FoodCheckerSearch> call, Throwable t) {
                FoodCheckServerUnreachableException e = new FoodCheckServerUnreachableException();
                Timber.d(e.getMessage());
                //foodChecker_event=new FoodCheckerEvent(barcode,"NotFound");
                getProductsCallback.onError(e);

            }
        });

    }

    private void testData(){
        getProduct("0016000264601", new GetProductCallback() {
            @Override
            public void onProductLoaded(FoodCheckerProduct FCProduct) {
                tvResult.append(FCProduct.toString());
            }

            @Override
            public void onError(Throwable throwable,String barcode) {
                tvResult.append(throwable.getMessage());
            }
        });
    }

    private void addEvenInDb(FoodCheckerEvent foodChecker_event){
        EventDataSource foodChecker_eventDataSource = EventDataSource.getInstance(getContentResolver());
        foodChecker_eventDataSource.saveEvent(foodChecker_event, new FoodCheckerEventSourceInterface.Local.SaveEventCallback() {
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
        /*Cursor cursor=getContentResolver().query(FoodCheckerEventContract.EventEntry.buildEventUri(),FoodCheckerEventContract.EventEntry.EVENT_COLUMNS,null,null,null);
        getDataTestEvent(cursor);*/
        Timber.d(fabFoodIntermediateLib.getAllData().get(0).getMParsableCategories().toString());
    }
    private List<FoodCheckerEvent> getDataTestEvent(Cursor cursor){
        Timber.d("Data in DB "+cursor.getCount() + " "+cursor.getColumnNames().length);
        List <FoodCheckerEvent> list=new ArrayList<>();
        for (int i=0;i<cursor.getColumnNames().length;i++)
            Timber.d(cursor.getColumnNames()[i]);
        if (cursor==null){
            return null;
        }
        int totalData=cursor.getCount();
        int colCount=cursor.getColumnCount();

        int indexmTimestamp=cursor.getColumnIndex("timestamp");
        int indexmBarcode=cursor.getColumnIndex("barcode");
        int indexmStatus=cursor.getColumnIndex("status");
        if (cursor.moveToFirst()){
            do {
                FoodCheckerEvent foodChecker_event =new FoodCheckerEvent(cursor.getLong(indexmTimestamp),
                        cursor.getString(indexmBarcode),cursor.getString(indexmStatus));
                Timber.d(foodChecker_event.toString());
                list.add(foodChecker_event);
                longToDate(foodChecker_event.getTimestamp());
            }while (cursor.moveToNext());

        }
        return list;
    }
    private void longToDate(Long data){
        Date date=new Date(data*1000);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
        String dateText = df2.format(date);
        Timber.d(dateText);
    }
}
