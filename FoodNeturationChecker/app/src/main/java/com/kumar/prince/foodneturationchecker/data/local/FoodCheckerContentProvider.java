package com.kumar.prince.foodneturationchecker.data.local;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/*
* Created by prince on 25/8/17.
* */

public class FC_ContentProvider extends android.content.ContentProvider {

    private static final int EVENT = 200;
    private static final int EVENT_ITEM = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FC_DbHelper mLocalFCDbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FC_EventContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FC_EventContract.EventEntry.TABLE_NAME, EVENT);
        matcher.addURI(authority, FC_EventContract.EventEntry.TABLE_NAME + "/*", EVENT_ITEM);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mLocalFCDbHelper = new FC_DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENT:
                return FC_EventContract.CONTENT_TYPE;
            case EVENT_ITEM:
                return FC_EventContract.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case EVENT:
                retCursor = mLocalFCDbHelper.getReadableDatabase().query(
                        FC_EventContract.EventEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case EVENT_ITEM:
                String[] where_event = {uri.getLastPathSegment()};
                retCursor = mLocalFCDbHelper.getReadableDatabase().query(
                        FC_EventContract.EventEntry.TABLE_NAME,
                        projection,
                        FC_EventContract.EventEntry._ID + " = ?",
                        where_event,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mLocalFCDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        Cursor exists;

        switch (match) {
            case EVENT:
                exists = db.query(
                        FC_EventContract.EventEntry.TABLE_NAME,
                        new String[]{FC_EventContract.EventEntry._ID},
                        FC_EventContract.EventEntry._ID + " = ?",
                        new String[]{values.getAsString(FC_EventContract.EventEntry._ID)},
                        null,
                        null,
                        null
                );
                if (exists.moveToLast()) {
                    long _id = db.update(
                            FC_EventContract.EventEntry.TABLE_NAME, values,
                            FC_EventContract.EventEntry._ID + " = ?",
                            new String[]{values.getAsString(FC_EventContract.EventEntry._ID)}
                    );
                    if (_id > 0) {
                        returnUri = FC_EventContract.EventEntry.buildEventUriWith(_id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                } else {
                    long _id = db.insert(FC_EventContract.EventEntry.TABLE_NAME, null, values);
                    if (_id > 0) {
                        returnUri = FC_EventContract.EventEntry.buildEventUriWith(_id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri + "   " + values.toString());
                    }
                }
                exists.close();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mLocalFCDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case EVENT:
                rowsDeleted = db.delete(
                        FC_EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mLocalFCDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case EVENT:
                rowsUpdated = db.update(FC_EventContract.EventEntry.TABLE_NAME, values, selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

}