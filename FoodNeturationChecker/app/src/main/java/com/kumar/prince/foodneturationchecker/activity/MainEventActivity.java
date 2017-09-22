package com.kumar.prince.foodneturationchecker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.kumar.prince.fabfoodlibrary.FabFoodIntermediateLib;
import com.kumar.prince.foodneturationchecker.Adapter.ViewPagerAdapter;
import com.kumar.prince.foodneturationchecker.Barcode.ScanBarcodeActivity;
import com.kumar.prince.foodneturationchecker.Error.FoodCheckerProductnotexistexception;
import com.kumar.prince.foodneturationchecker.Error.FoodCheckServerUnreachableException;
import com.kumar.prince.foodneturationchecker.Fragment.FragmentA;
import com.kumar.prince.foodneturationchecker.Fragment.FragmentB;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerOpenFoodFactsAPIClient;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerProductBarcode;
import com.kumar.prince.foodneturationchecker.communication.NetworkConnectivity;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FoodCheckerEventSourceInterface;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FoodCheckerProductSourceInterface;
import com.kumar.prince.foodneturationchecker.data.local.EventDataSource;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerEvent;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerProduct;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.kumar.prince.foodneturationchecker.Barcode.ScanBarcodeActivity.AUTOFOCUS_ENABLE;
import static com.kumar.prince.foodneturationchecker.Barcode.ScanBarcodeActivity.GETRESULT;
import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_NO_NETWORK;
import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_OK;

public class MainEventActivity extends AppCompatActivity implements FoodCheckerProductSourceInterface,FoodCheckerProductSourceInterface.GetProductCallback {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    FabFoodIntermediateLib fabFoodIntermediateLib;
    FoodCheckerEvent foodChecker_event;

    private int[] tabIcons = {
            R.drawable.ic_history_black_24dp,
            R.drawable.ic_favorite_black_24dp

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fabFoodIntermediateLib=new FabFoodIntermediateLib(this);
        fabFoodIntermediateLib.dbInitialize();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    private void scanBarcode(){
        NetworkConnectivity networkConnectivity=new NetworkConnectivity();
        if (networkConnectivity.isConnected(this)){
            int barCodeRequestCode = 1000;
            Intent intent = new Intent(this, ScanBarcodeActivity.class);
            intent.putExtra(AUTOFOCUS_ENABLE,true);
            startActivityForResult(intent,barCodeRequestCode);
        }else {
            displayMessage(getCurrentFocus(),STATUS_NO_NETWORK);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String message="";
        if(requestCode==1000){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data!=null){
                    Barcode barcode = data.getParcelableExtra(GETRESULT);
                    Timber.d(barcode.displayValue);
                    //message="20199876"/*barcode.displayValue*/;
                    message=barcode.displayValue;
                    getProduct(message,this);
                }
            }
        }

        displayMessage(viewPager.findFocus(),message);

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void getProduct(@NonNull final String barcode, @NonNull final GetProductCallback getProductCallback) {
        FoodCheckerOpenFoodFactsAPIClient FCOpenFoodFactsAPIClient = new FoodCheckerOpenFoodFactsAPIClient(FoodCheckerOpenFoodFactsAPIClient.ENDPOINT_BARCODE);
        Call<FoodCheckerProductBarcode> call = FCOpenFoodFactsAPIClient.getProduct(barcode);

        call.enqueue(new Callback<FoodCheckerProductBarcode>() {
            @Override
            public void onResponse(Call<FoodCheckerProductBarcode> call, Response<FoodCheckerProductBarcode> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    FoodCheckServerUnreachableException e = new FoodCheckServerUnreachableException();
                    Timber.w(e);
                    getProductCallback.onError(e,barcode);
                    return;
                }
                FoodCheckerProductBarcode foodChecker_productBarcode = response.body();
                Timber.d(response.message()+" "+ foodChecker_productBarcode.toString());
                if (foodChecker_productBarcode.getStatus() != 1) {
                    FoodCheckerProductnotexistexception e = new FoodCheckerProductnotexistexception();
                    Timber.w(e);
                    getProductCallback.onError(e,barcode);
                    return;
                }
                FoodCheckerProduct foodChecker_product = foodChecker_productBarcode.getFCProduct();
                Timber.w(response.message()+" "+ foodChecker_product.toString());
                foodChecker_event =new FoodCheckerEvent(barcode,"Found");
                //addEvenInDb(foodChecker_event);
                getProductCallback.onProductLoaded(foodChecker_product);

            }

            @Override
            public void onFailure(Call<FoodCheckerProductBarcode> call, Throwable t) {
                FoodCheckServerUnreachableException e = new FoodCheckServerUnreachableException();
                Timber.d(e.getMessage());
                String value=getResources().getString(R.string.not_found);
                foodChecker_event =new FoodCheckerEvent(barcode,value);
                getProductCallback.onError(e,barcode);


            }
        });
    }



    @Override
    public void getProducts(@NonNull String categoryKey, @NonNull String nutritionGradeValue, @NonNull GetProductsCallback getProductsCallback) {

    }

    /**
     * @param foodChecker_event
     */
    private void addEvenInDb(FoodCheckerEvent foodChecker_event){
        EventDataSource foodChecker_eventDataSource = EventDataSource.getInstance(getContentResolver());
        foodChecker_eventDataSource.saveEvent(foodChecker_event, new FoodCheckerEventSourceInterface.Local.SaveEventCallback() {
            @Override
            public void onEventSaved() {
                Timber.d("DataSaved");
                displayMessage(getCurrentFocus(),getResources().getString(R.string.event_Saved));

            }

            @Override
            public void onError() {
                Timber.d("Error");
                displayMessage(getCurrentFocus(),getResources().getString(R.string.event_not_Saved));
            }
        });

    }


    private void displayMessage(View view,String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((FragmentA)page).restartLoader();
        }else if (viewPager.getCurrentItem() == 1 && page != null){
            ((FragmentB)page).restartLoader();
        }

    }

    /**
     * @param foodChecker_Product
     */
    @Override
    public void onProductLoaded(FoodCheckerProduct foodChecker_Product) {
        Timber.d(foodChecker_Product.toString());
        FoodCheckerEvent foodChecker_event =new FoodCheckerEvent(foodChecker_Product.getmBarcode(),STATUS_OK);
        addEvenInDb(foodChecker_event);

    }

    /**
     * @param throwable
     * @param barcode
     */
    @Override
    public void onError(Throwable throwable,String barcode) {
        Timber.e(throwable.getMessage());
        FoodCheckerEvent foodChecker_event =new FoodCheckerEvent(barcode,throwable.getMessage());
        addEvenInDb(foodChecker_event);

    }
}
