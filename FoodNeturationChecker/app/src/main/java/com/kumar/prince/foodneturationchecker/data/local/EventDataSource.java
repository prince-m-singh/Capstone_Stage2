package com.kumar.prince.foodneturationchecker.data.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;


import com.kumar.prince.foodneturationchecker.data.FoodCheckerEventValues;
import com.kumar.prince.foodneturationchecker.data.callbackinterface.FoodCheckerEventSourceInterface;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerEvent;

import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by prince on 26/8/17.
 * Concrete implementation of a data source as a db.
 */
public class EventDataSource implements FoodCheckerEventSourceInterface.Local {

    private static EventDataSource INSTANCE;

    private ContentResolver mContentResolver;

    // Prevent direct instantiation.
    private EventDataSource(@NonNull ContentResolver contentResolver) {
        checkNotNull(contentResolver);
        mContentResolver = contentResolver;
    }

    public static EventDataSource getInstance(@NonNull ContentResolver contentResolver) {
        if (INSTANCE == null) {
            INSTANCE = new EventDataSource(contentResolver);
        }
        return INSTANCE;
    }

    private void checkExistEvent(@NonNull String barcode, @NonNull CheckExistEventCallback checkExistEventCallback) {
       Timber.d( "checkExistEvent");

        Cursor cursor = mContentResolver.query(
                FoodCheckerEventContract.EventEntry.buildEventUri(),
                null,
                FoodCheckerEventContract.EventEntry.COLUMN_NAME_BARCODE + " = ?",
                new String[]{barcode},
                null);

        if (cursor != null){
            if (cursor.moveToLast()) {
                long _id = cursor.getLong(cursor.getColumnIndex(FoodCheckerEventContract.EventEntry._ID));
                checkExistEventCallback.onEventExisted(_id);
            } else {
                checkExistEventCallback.onEventNotExisted();
            }
            cursor.close();
        } else {
            checkExistEventCallback.onEventNotExisted();
        }
    }

    private void addEvent(@NonNull FoodCheckerEvent FoodCheckerEvent, @NonNull AddEventCallback addEventCallback) {
       Timber.d( "addEvent");

        checkNotNull(FoodCheckerEvent);

        ContentValues values = FoodCheckerEventValues.from(FoodCheckerEvent);
        Uri uri = mContentResolver.insert(FoodCheckerEventContract.EventEntry.buildEventUri(), values);

        if (uri != null) {
            mContentResolver.notifyChange(uri, null, false);
            addEventCallback.onEventAdded();
        } else {
            addEventCallback.onError();
        }
    }

    private void updateEvent(@NonNull FoodCheckerEvent FoodCheckerEvent, @NonNull UpdateEventCallback updateEventCallback) {
       Timber.d( "updateEvent");

        checkNotNull(FoodCheckerEvent);

        ContentValues values = FoodCheckerEventValues.from(FoodCheckerEvent);

        String selection = FoodCheckerEventContract.EventEntry._ID + " LIKE ?";
        String[] selectionArgs = {FoodCheckerEvent.getAsStringId()};

        int rows = mContentResolver.update(FoodCheckerEventContract.EventEntry.buildEventUri(), values, selection, selectionArgs);

        if (rows != 0) {
            mContentResolver.notifyChange(FoodCheckerEventContract.EventEntry.buildEventUri(),null);
            updateEventCallback.onEventUpdated();
        } else {
            updateEventCallback.onError();
        }
    }

    @Override
    public void saveEvent(@NonNull final FoodCheckerEvent FoodCheckerEvent, @NonNull final SaveEventCallback saveEventCallback) {
       Timber.d( "saveEvent");

        checkNotNull(FoodCheckerEvent);

        String barcode = FoodCheckerEvent.getBarcode();

        checkExistEvent(barcode, new CheckExistEventCallback() {

            @Override
            public void onEventExisted(long id) {
                FoodCheckerEvent.setId(id);
                updateEvent(FoodCheckerEvent, new UpdateEventCallback() {
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
                addEvent(FoodCheckerEvent, new AddEventCallback() {
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
