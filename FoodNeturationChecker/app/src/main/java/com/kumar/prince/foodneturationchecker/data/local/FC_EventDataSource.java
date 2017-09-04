package com.kumar.prince.foodneturationchecker.data.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;


import com.kumar.prince.foodneturationchecker.data.FC_EventValues;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FC_EventSourceInterface;
import com.kumar.prince.foodneturationchecker.data.model.FC_Event;

import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by prince on 26/8/17.
 * Concrete implementation of a data source as a db.
 */
public class FC_EventDataSource implements FC_EventSourceInterface.Local {

    private static FC_EventDataSource INSTANCE;

    private ContentResolver mContentResolver;

    // Prevent direct instantiation.
    private FC_EventDataSource(@NonNull ContentResolver contentResolver) {
        checkNotNull(contentResolver);
        mContentResolver = contentResolver;
    }

    public static FC_EventDataSource getInstance(@NonNull ContentResolver contentResolver) {
        if (INSTANCE == null) {
            INSTANCE = new FC_EventDataSource(contentResolver);
        }
        return INSTANCE;
    }

    private void checkExistEvent(@NonNull String barcode, @NonNull CheckExistEventCallback checkExistEventCallback) {
       Timber.d( "checkExistEvent");

        Cursor cursor = mContentResolver.query(
                FC_EventContract.EventEntry.buildEventUri(),
                null,
                FC_EventContract.EventEntry.COLUMN_NAME_BARCODE + " = ?",
                new String[]{barcode},
                null);

        if (cursor != null){
            if (cursor.moveToLast()) {
                long _id = cursor.getLong(cursor.getColumnIndex(FC_EventContract.EventEntry._ID));
                checkExistEventCallback.onEventExisted(_id);
            } else {
                checkExistEventCallback.onEventNotExisted();
            }
            cursor.close();
        } else {
            checkExistEventCallback.onEventNotExisted();
        }
    }

    private void addEvent(@NonNull FC_Event FC_Event, @NonNull AddEventCallback addEventCallback) {
       Timber.d( "addEvent");

        checkNotNull(FC_Event);

        ContentValues values = FC_EventValues.from(FC_Event);
        Uri uri = mContentResolver.insert(FC_EventContract.EventEntry.buildEventUri(), values);

        if (uri != null) {
            addEventCallback.onEventAdded();
        } else {
            addEventCallback.onError();
        }
    }

    private void updateEvent(@NonNull FC_Event FC_Event, @NonNull UpdateEventCallback updateEventCallback) {
       Timber.d( "updateEvent");

        checkNotNull(FC_Event);

        ContentValues values = FC_EventValues.from(FC_Event);

        String selection = FC_EventContract.EventEntry._ID + " LIKE ?";
        String[] selectionArgs = {FC_Event.getAsStringId()};

        int rows = mContentResolver.update(FC_EventContract.EventEntry.buildEventUri(), values, selection, selectionArgs);

        if (rows != 0) {
            updateEventCallback.onEventUpdated();
        } else {
            updateEventCallback.onError();
        }
    }

    @Override
    public void saveEvent(@NonNull final FC_Event FC_Event, @NonNull final SaveEventCallback saveEventCallback) {
       Timber.d( "saveEvent");

        checkNotNull(FC_Event);

        String barcode = FC_Event.getBarcode();

        checkExistEvent(barcode, new CheckExistEventCallback() {

            @Override
            public void onEventExisted(long id) {
                FC_Event.setId(id);
                updateEvent(FC_Event, new UpdateEventCallback() {
                    @Override
                    public void onEventUpdated() {
                       Timber.d( "onEventExisted - onEventUpdated");
                        saveEventCallback.onEventSaved();
                    }

                    @Override
                    public void onError() {
                       Timber.d( "onEventExisted - onError");
                        saveEventCallback.onError();
                    }
                });
            }

            @Override
            public void onEventNotExisted() {
                addEvent(FC_Event, new AddEventCallback() {
                    @Override
                    public void onEventAdded() {
                       Timber.d( "onEventNotExisted - onEventAdded");
                        saveEventCallback.onEventSaved();
                    }

                    @Override
                    public void onError() {
                       Timber.d( "onEventNotExisted - onError");
                        saveEventCallback.onError();
                    }
                });
            }
        });
    }

    interface CheckExistEventCallback {

        void onEventExisted(long id);

        void onEventNotExisted();

    }

    interface AddEventCallback {

        void onEventAdded();

        void onError();

    }

    interface UpdateEventCallback {

        void onEventUpdated();

        void onError();

    }
}
