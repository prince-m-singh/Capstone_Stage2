package com.kumar.prince.foodneturationchecker.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kumar.prince.foodneturationchecker.Adapter.CustomAdapter;
import com.kumar.prince.foodneturationchecker.Adapter.EventRecylerViewAdapter;
import com.kumar.prince.foodneturationchecker.Error.FC_ProductNotExistException;
import com.kumar.prince.foodneturationchecker.Error.FC_ServerUnreachableException;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.activity.FoodDetailsActivity;
import com.kumar.prince.foodneturationchecker.communication.FC_OpenFoodFactsAPIClient;
import com.kumar.prince.foodneturationchecker.communication.FC_ProductBarcode;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FC_EventSourceInterface;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FC_ProductSourceInterface;
import com.kumar.prince.foodneturationchecker.data.local.FC_EventContract;
import com.kumar.prince.foodneturationchecker.data.local.FC_EventDataSource;
import com.kumar.prince.foodneturationchecker.data.model.FC_Event;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;

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
    private List<FC_Event> fc_eventList;
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
    public void onClick(FC_Event fc_event) {
        Snackbar.make(this.getView(), fc_event.getBarcode()+":- "+ fc_event.getStatus(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
       Timber.d(fc_event.getBarcode());
       if (dataFoundStatus(fc_event)){
           getAllDataOfBarCode(fc_event.getBarcode());
       }else {
           Snackbar.make(this.getView(), fc_event.getBarcode()+":- "+ fc_event.getStatus(), Snackbar.LENGTH_LONG)
                   .setAction("Action", null).show();
       }

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
                    Cursor cursor=this.getContext().getContentResolver().query(FC_EventContract.EventEntry.buildEventUri(), FC_EventContract.EventEntry.EVENT_COLUMNS,
                            null,null, FC_EventContract.EventEntry.COLUMN_NAME_TIMESTAMP+" DESC");
                    //cursor.setNotificationUri(this.getContext().getContentResolver(), FC_EventContract.EventEntry.buildEventUri());
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

        fc_eventList=getDataTestEvent(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private List<FC_Event> getDataTestEvent(Cursor cursor){
        Timber.d("Data in DB "+cursor.getCount() + " "+cursor.getColumnNames().length);
        List <FC_Event> list=new ArrayList<>();
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
                FC_Event fc_event=new FC_Event(cursor.getLong(indexmTimestamp),
                        cursor.getString(indexmBarcode),cursor.getString(indexmStatus));
                Timber.d(fc_event.toString());
                list.add(fc_event);
                longToDate(fc_event.getTimestamp());
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
                    //getProductCallback.onError(e,barcode);
                    return;
                }
                FC_Product fc_product=fc_productBarcode.getFCProduct();
                Timber.w(response.message()+" "+fc_product.toString());
                newActivityStart(fc_product);
                /*fc_event=new FC_Event(barcode,"Found");
                //addEvenInDb(fc_event);
                getProductCallback.onProductLoaded(fc_product);*/

            }

            @Override
            public void onFailure(Call<FC_ProductBarcode> call, Throwable t) {
                FC_ServerUnreachableException e = new FC_ServerUnreachableException();
                Timber.d(e.getMessage());
                /*fc_event=new FC_Event(barcode,"NotFound");
                getProductCallback.onError(e,barcode);*/


            }
        });


    }

    private boolean dataFoundStatus(FC_Event fc_event){
        if (fc_event.getStatus().equals(STATUS_OK))
            return true;
        else if (fc_event.getStatus().equals(STATUS_SERVER_UNREACHABLE))
            return true;
        return false;
    }

    private void newActivityStart(FC_Product fc_product){
        Intent i = new Intent(getActivity(), FoodDetailsActivity.class);
        i.putExtra("sampleObject", fc_product);
        startActivity(i);
    }
}

