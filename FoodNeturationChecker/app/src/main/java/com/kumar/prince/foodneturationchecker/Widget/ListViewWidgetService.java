package com.kumar.prince.foodneturationchecker.Widget;

import android.content.Intent;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.kumar.prince.fabfoodlibrary.FabFoodEntity;
import com.kumar.prince.fabfoodlibrary.FabFoodIntermediateLib;
import com.kumar.prince.foodneturationchecker.R;

import java.util.List;

import timber.log.Timber;

/**
 * Created by prince on 19/9/17.
 */

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            public static final int NOTIFICATION_ID = 1;
            FabFoodIntermediateLib fabFoodIntermediateLib;
            List<FabFoodEntity> fabFoodEntityList1;

            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (fabFoodEntityList1 != null) {

                }
                final long identityToken = Binder.clearCallingIdentity();
                fabFoodIntermediateLib = new FabFoodIntermediateLib(getBaseContext());
                fabFoodIntermediateLib.dbInitialize();
                fabFoodEntityList1 = fabFoodIntermediateLib.getAllData();
                Timber.d("Size " + fabFoodEntityList1.size());
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (fabFoodEntityList1 != null) {
                    fabFoodEntityList1 = null;
                }
            }

            @Override
            public int getCount() {
                return fabFoodEntityList1 == null ? 0 : fabFoodEntityList1.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        fabFoodEntityList1 == null || fabFoodEntityList1.size() < position) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_layout);
                views.setTextViewText(R.id.widget_item_title, fabFoodEntityList1.get(position).getMProductName());

                views.setImageViewResource(R.id.widget_item_icon, R.drawable.ic_food_found_green_24dp);
                views.setTextViewText(R.id.widget_item_subtitle, fabFoodEntityList1.get(position).getMBarcode());
                final Intent fillInIntent = new Intent();
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_layout);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (fabFoodEntityList1.get(position) != null) {
                    return position;
                } else {
                    return position;
                }
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}