package com.kumar.prince.foodneturationchecker.data.local;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by prince on 25/8/17.
 * The contract used for the db to save the category locally.
 */
public final class FoodCheckerEventContract {

    public static final String CONTENT_AUTHORITY = "com.kumar.prince.foodneturationchecker";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private FoodCheckerEventContract() {}

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(EventEntry.TABLE_NAME).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + EventEntry.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + EventEntry.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class EventEntry implements BaseColumns {

        public static final String TABLE_NAME = "event";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_BARCODE = "barcode";
        public static final String COLUMN_NAME_STATUS = "status";
        public static String[] EVENT_COLUMNS = new String[]{
                FoodCheckerEventContract.EventEntry._ID,
                FoodCheckerEventContract.EventEntry.COLUMN_NAME_TIMESTAMP,
                FoodCheckerEventContract.EventEntry.COLUMN_NAME_BARCODE,
                FoodCheckerEventContract.EventEntry.COLUMN_NAME_STATUS};

        public static Uri buildEventUriWith(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildEventUri() {
            return CONTENT_URI.buildUpon().build();
        }

    }

}