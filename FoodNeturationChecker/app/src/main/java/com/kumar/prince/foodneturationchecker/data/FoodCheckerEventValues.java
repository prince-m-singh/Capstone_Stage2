package com.kumar.prince.foodneturationchecker.data;

import android.content.ContentValues;

import com.kumar.prince.foodneturationchecker.data.local.FoodCheckerEventContract;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerEvent;


public class FoodCheckerEventValues {

    public static ContentValues from(FoodCheckerEvent FCEvent) {
        ContentValues values = new ContentValues();
        values.put(FoodCheckerEventContract.EventEntry._ID, FCEvent.getId());
        values.put(FoodCheckerEventContract.EventEntry.COLUMN_NAME_TIMESTAMP, FCEvent.getTimestamp());
        values.put(FoodCheckerEventContract.EventEntry.COLUMN_NAME_BARCODE, FCEvent.getBarcode());
        values.put(FoodCheckerEventContract.EventEntry.COLUMN_NAME_STATUS, FCEvent.getStatus());
        return values;
    }
}
