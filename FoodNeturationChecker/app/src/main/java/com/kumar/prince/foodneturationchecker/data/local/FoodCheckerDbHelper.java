package com.kumar.prince.foodneturationchecker.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by prince on 25/8/17.
 * */

public class FC_DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Local.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String PRIMARY_KEY = " PRIMARY KEY";

    private static final String CREATE_TABLE = "CREATE TABLE ";

    private static final String FOREIGN_KEY = " FOREIGN KEY";

    private static final String REFERENCES = " REFERENCES ";

    private static final String OPEN_PARENTHESIS = " (";

    private static final String CLOSE_PARENTHESIS = " )";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_EVENT =
            CREATE_TABLE + FC_EventContract.EventEntry.TABLE_NAME + OPEN_PARENTHESIS +
                    FC_EventContract.EventEntry._ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    FC_EventContract.EventEntry.COLUMN_NAME_TIMESTAMP + TEXT_TYPE + COMMA_SEP +
                    FC_EventContract.EventEntry.COLUMN_NAME_BARCODE + TEXT_TYPE + COMMA_SEP +
                    FC_EventContract.EventEntry.COLUMN_NAME_STATUS + TEXT_TYPE +
                    CLOSE_PARENTHESIS;

    public FC_DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EVENT);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}