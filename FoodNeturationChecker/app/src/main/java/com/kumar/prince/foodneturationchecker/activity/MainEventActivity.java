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
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.kumar.prince.fabfoodlibrary.FabFoodIntermediateLib;
import com.kumar.prince.foodneturationchecker.Adapter.ViewPagerAdapter;
import com.kumar.prince.foodneturationchecker.Barcode.ScanBarcodeActivity;
import com.kumar.prince.foodneturationchecker.Error.FC_ProductNotExistException;
import com.kumar.prince.foodneturationchecker.Error.FC_ServerUnreachableException;
import com.kumar.prince.foodneturationchecker.Fragment.FragmentA;
import com.kumar.prince.foodneturationchecker.Fragment.FragmentB;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.communication.FC_OpenFoodFactsAPIClient;
import com.kumar.prince.foodneturationchecker.communication.FC_ProductBarcode;
import com.kumar.prince.foodneturationchecker.communication.NetworkConnectivity;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FC_EventSourceInterface;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FC_ProductSourceInterface;
import com.kumar.prince.foodneturationchecker.data.local.FC_EventDataSource;
import com.kumar.prince.foodneturationchecker.data.model.FC_Event;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.kumar.prince.foodneturationchecker.Barcode.ScanBarcodeActivity.AUTOFOCUS_ENABLE;
import static com.kumar.prince.foodneturationchecker.Barcode.ScanBarcodeActivity.GETRESULT;
import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_NO_NETWORK;
import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_OK;

public class MainEventActivity extends AppCompatActivity implements FC_ProductSourceInterface,FC_ProductSourceInterface.GetProductCallback {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    FabFoodIntermediateLib fabFoodIntermediateLib;
    FC_Event fc_event;

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
                    //message="0016000264601"/*barcode.displayValue*/;
                    getProduct(barcode.displayValue,this);
                }
            }
        }

        displayMessage(viewPager.findFocus(),message);

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void getProduct(@NonNull String barcode, @NonNull GetProductCallback getProductCallback) {
        FC_OpenFoodFactsAPIClient FCOpenFoodFactsAPIClient = new FC_OpenFoodFactsAPIClient(FC_OpenFoodFactsAPIClient.ENDPOINT_BARCODE);
        Call<FC_ProductBarcode> call = FCOpenFoodFactsAPIClient.getProduct(barcode);

        call.enqueue(new Callback<FC_ProductBarcode>() {
            @Override
            public void onResponse(Call<FC_ProductBarcode> call, Response<FC_ProductBarcode> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    FC_ServerUnreachableException e = new FC_ServerUnreachableException();
                    Timber.w(e);
                    getProductCallback.onError(e,barcode);
                    return;
                }
                FC_ProductBarcode fc_productBarcode = response.body();
                Timber.d(response.message()+" "+fc_productBarcode.toString());
                if (fc_productBarcode.getStatus() != 1) {
                    FC_ProductNotExistException e = new FC_ProductNotExistException();
                    Timber.w(e);
                    getProductCallback.onError(e,barcode);
                    return;
                }
                FC_Product fc_product=fc_productBarcode.getFCProduct();
                Timber.w(response.message()+" "+fc_product.toString());
                fc_event=new FC_Event(barcode,"Found");
                //addEvenInDb(fc_event);
                getProductCallback.onProductLoaded(fc_product);

            }

            @Override
            public void onFailure(Call<FC_ProductBarcode> call, Throwable t) {
                FC_ServerUnreachableException e = new FC_ServerUnreachableException();
                Timber.d(e.getMessage());
                fc_event=new FC_Event(barcode,"NotFound");
                getProductCallback.onError(e,barcode);


            }
        });
    }



    @Override
    public void getProducts(@NonNull String categoryKey, @NonNull String nutritionGradeValue, @NonNull GetProductsCallback getProductsCallback) {

    }

    /**
     * @param fc_event
     */
    private void addEvenInDb(FC_Event fc_event){
        FC_EventDataSource fc_eventDataSource=FC_EventDataSource.getInstance(getContentResolver());
        fc_eventDataSource.saveEvent(fc_event, new FC_EventSourceInterface.Local.SaveEventCallback() {
            @Override
            public void onEventSaved() {
                Timber.d("DataSaved");
                displayMessage(getCurrentFocus(),"Event Saved");

            }

            @Override
            public void onError() {
                Timber.d("Error");
                displayMessage(getCurrentFocus(),"Event Not Saved");
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
     * @param fc_Product
     */
    @Override
    public void onProductLoaded(FC_Product fc_Product) {
        Timber.d(fc_Product.toString());
        FC_Event fc_event=new FC_Event(fc_Product.getmBarcode(),STATUS_OK);
        addEvenInDb(fc_event);

    }

    /**
     * @param throwable
     * @param barcode
     */
    @Override
    public void onError(Throwable throwable,String barcode) {
        Timber.e(throwable.getMessage());
        FC_Event fc_event=new FC_Event(barcode,throwable.getMessage());
        addEvenInDb(fc_event);

    }
}
