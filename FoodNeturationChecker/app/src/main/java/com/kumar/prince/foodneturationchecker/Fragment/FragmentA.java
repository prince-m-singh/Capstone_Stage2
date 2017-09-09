package com.kumar.prince.foodneturationchecker.Fragment;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kumar.prince.foodneturationchecker.Adapter.CustomAdapter;
import com.kumar.prince.foodneturationchecker.Adapter.EventRecylerViewAdapter;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.data.local.FC_EventContract;
import com.kumar.prince.foodneturationchecker.data.model.FC_Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

/**
 * Created by prince on 14/9/17.
 */

public class FragmentA extends Fragment  implements
        EventRecylerViewAdapter.EventAdapterOnClickHandler,LoaderManager.LoaderCallbacks<Cursor> {

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
    public void onClick(FC_Event fc_event) {
        Snackbar.make(this.getView(), fc_event.getBarcode()+":- "+ fc_event.getStatus(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
       Timber.d(fc_event.getBarcode());
    }

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
                    return this.getContext().getContentResolver().query(FC_EventContract.EventEntry.buildEventUri(), FC_EventContract.EventEntry.EVENT_COLUMNS,null,null,null);

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
}

