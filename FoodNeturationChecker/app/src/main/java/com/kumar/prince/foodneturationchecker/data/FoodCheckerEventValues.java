package com.kumar.prince.foodneturationchecker.data;

import android.content.ContentValues;

import com.kumar.prince.foodneturationchecker.data.local.FC_EventContract;
import com.kumar.prince.foodneturationchecker.data.model.FC_Event;


public class FC_EventValues {

    public static ContentValues from(FC_Event FCEvent) {
        ContentValues values = new ContentValues();
        values.put(FC_EventContract.EventEntry._ID, FCEvent.getId());
        values.put(FC_EventContract.EventEntry.COLUMN_NAME_TIMESTAMP, FCEvent.getTimestamp());
        values.put(FC_EventContract.EventEntry.COLUMN_NAME_BARCODE, FCEvent.getBarcode());
        values.put(FC_EventContract.EventEntry.COLUMN_NAME_STATUS, FCEvent.getStatus());
        return values;
    }
}
