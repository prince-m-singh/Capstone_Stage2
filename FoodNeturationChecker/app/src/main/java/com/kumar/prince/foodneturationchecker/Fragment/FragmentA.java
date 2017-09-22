package com.kumar.prince.foodneturationchecker.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kumar.prince.foodneturationchecker.Adapter.EventRecylerViewAdapter;
import com.kumar.prince.foodneturationchecker.Error.FoodCheckerProductnotexistexception;
import com.kumar.prince.foodneturationchecker.Error.FoodCheckServerUnreachableException;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.activity.FoodDetailsActivity;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerOpenFoodFactsAPIClient;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerProductBarcode;
import com.kumar.prince.foodneturationchecker.data.local.FoodCheckerEventContract;
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

import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_OK;
import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_SERVER_UNREACHABLE;

/**
 * Created by prince on 14/9/17.
 */

public class FragmentA extends Fragment  implements
        EventRecylerViewAdapter.EventAdapterOnClickHandler,
    LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private CardView cardView;
    private List<FoodCheckerEvent> foodChecker_eventList;
    RecyclerView.LayoutManager mLayoutManager;
    EventRecylerViewAdapter eventRecylerViewAdapter;
    private static final int TASK_LOADER_ID=0;
    //private static final String image="R.dra"
    public FragmentA() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventRecylerViewAdapter = new EventRecylerViewAdapter(this);
        this.getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID,null,this);

        getLoaderManager().restartLoader(TASK_LOADER_ID,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        Timber.d( "The application stopped after this");
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        /*list.setLayoutManager(llm);
        list.setAdapter( adapter );*/
        // ListView
        recyclerView.setLayoutManager(llm);
        Timber.d( "Set Layout");
        Timber.d( "Adapter instance");
        recyclerView.setAdapter(eventRecylerViewAdapter);
        Timber.d( "Adapter");


        return view;
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(TASK_LOADER_ID,null,this);
        super.onResume();

       // getLoaderManager().getLoader(TASK_LOADER_ID).forceLoad();
    }

    @Override
    public void onClick(FoodCheckerEvent foodChecker_event) {
        Snackbar.make(this.getView(), foodChecker_event.getBarcode()+":- "+ foodChecker_event.getStatus(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
       Timber.d(foodChecker_event.getBarcode());
//       addEvenInDb(foodChecker_event);
       if (dataFoundStatus(foodChecker_event)){
           getAllDataOfBarCode(foodChecker_event.getBarcode());
       }else {
           Snackbar.make(this.getView(), foodChecker_event.getBarcode()+":- "+ foodChecker_event.getStatus(), Snackbar.LENGTH_LONG)
                   .setAction("Action", null).show();
       }
  //      getLoaderManager().restartLoader(TASK_LOADER_ID,null,this);


    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this.getActivity()) {
            Cursor eventData=null;

            @Override
            protected void onStartLoading() {
                if (eventData!=null){
                    deliverResult(eventData);
                }else {
                    forceLoad();
                }
             //   super.onStartLoading();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    Cursor cursor=this.getContext().getContentResolver().query(FoodCheckerEventContract.EventEntry.buildEventUri(), FoodCheckerEventContract.EventEntry.EVENT_COLUMNS,
                            null,null, FoodCheckerEventContract.EventEntry.COLUMN_NAME_TIMESTAMP+" DESC");
                    //cursor.setNotificationUri(this.getContext().getContentResolver(), FoodCheckerEventContract.EventEntry.buildEventUri());
                    return cursor;

                }catch (Exception ex){
                    ex.printStackTrace();
                    return null;

                }

            }

            @Override
            public void deliverResult(Cursor data) {
                eventData=data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        foodChecker_eventList =getDataTestEvent(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
        eventRecylerViewAdapter.setEventData(list);
        return list;
    }
    private void longToDate(Long data){
        Date date=new Date(data*1000);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
        String dateText = df2.format(date);
        Timber.d(dateText);
    }

    public void restartLoader(){
        getLoaderManager().restartLoader(TASK_LOADER_ID,null,this);
    }


    private void getAllDataOfBarCode(String barcode){
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
                Timber.d(response.message()+" "+ foodChecker_productBarcode.toString());
                if (foodChecker_productBarcode.getStatus() != 1) {
                    FoodCheckerProductnotexistexception e = new FoodCheckerProductnotexistexception();
                    Timber.w(e);
                    //getProductCallback.onError(e,barcode);
                    return;
                }
                FoodCheckerProduct foodChecker_product = foodChecker_productBarcode.getFCProduct();
                Timber.w(response.message()+" "+ foodChecker_product.toString());
                //addEvenInDb(foodChecker_event);
                newActivityStart(foodChecker_product);
                /*foodChecker_event=new FoodCheckerEvent(barcode,"Found");
                //addEvenInDb(foodChecker_event);
                getProductCallback.onProductLoaded(foodChecker_product);*/

            }

            @Override
            public void onFailure(Call<FoodCheckerProductBarcode> call, Throwable t) {
                FoodCheckServerUnreachableException e = new FoodCheckServerUnreachableException();
                Timber.d(e.getMessage());
                /*foodChecker_event=new FoodCheckerEvent(barcode,"NotFound");
                getProductCallback.onError(e,barcode);*/


            }
        });


    }

    private boolean dataFoundStatus(FoodCheckerEvent foodChecker_event){
        if (foodChecker_event.getStatus().equals(STATUS_OK))
            return true;
        else if (foodChecker_event.getStatus().equals(STATUS_SERVER_UNREACHABLE))
            return true;
        return false;
    }

    private void newActivityStart(FoodCheckerProduct foodChecker_product){
        Intent i = new Intent(getActivity(), FoodDetailsActivity.class);
        i.putExtra("sampleObject", foodChecker_product);
        startActivity(i);
    }


}

