package com.example.ian.timecardcapstone.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.example.ian.timecardcapstone.BuildConfig;
import com.example.ian.timecardcapstone.provider.base.BaseContentProvider;
import com.example.ian.timecardcapstone.provider.rosterappsdata.RosterappsdataColumns;
import com.example.ian.timecardcapstone.provider.shift.ShiftColumns;

import java.util.Arrays;

public class ShiftProvider extends BaseContentProvider {
    private static final String TAG = ShiftProvider.class.getSimpleName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";

    public static final String AUTHORITY = "com.example.ian.timecardcapstone.provider";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;

    private static final int URI_TYPE_ROSTERAPPSDATA = 0;
    private static final int URI_TYPE_ROSTERAPPSDATA_ID = 1;

    private static final int URI_TYPE_SHIFT = 2;
    private static final int URI_TYPE_SHIFT_ID = 3;



    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, RosterappsdataColumns.TABLE_NAME, URI_TYPE_ROSTERAPPSDATA);
        URI_MATCHER.addURI(AUTHORITY, RosterappsdataColumns.TABLE_NAME + "/#", URI_TYPE_ROSTERAPPSDATA_ID);
        URI_MATCHER.addURI(AUTHORITY, ShiftColumns.TABLE_NAME, URI_TYPE_SHIFT);
        URI_MATCHER.addURI(AUTHORITY, ShiftColumns.TABLE_NAME + "/#", URI_TYPE_SHIFT_ID);
    }

    @Override
    protected SQLiteOpenHelper createSqLiteOpenHelper() {
        return ShiftDatabaseSQLiteOpenHelper.getInstance(getContext());
    }

    @Override
    protected boolean hasDebug() {
        return DEBUG;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_ROSTERAPPSDATA:
                return TYPE_CURSOR_DIR + RosterappsdataColumns.TABLE_NAME;
            case URI_TYPE_ROSTERAPPSDATA_ID:
                return TYPE_CURSOR_ITEM + RosterappsdataColumns.TABLE_NAME;

            case URI_TYPE_SHIFT:
                return TYPE_CURSOR_DIR + ShiftColumns.TABLE_NAME;
            case URI_TYPE_SHIFT_ID:
                return TYPE_CURSOR_ITEM + ShiftColumns.TABLE_NAME;

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        return super.insert(uri, values);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.update(uri, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
                    + " groupBy=" + uri.getQueryParameter(QUERY_GROUP_BY) + " having=" + uri.getQueryParameter(QUERY_HAVING) + " limit=" + uri.getQueryParameter(QUERY_LIMIT));
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected QueryParams getQueryParams(Uri uri, String selection, String[] projection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_ROSTERAPPSDATA:
            case URI_TYPE_ROSTERAPPSDATA_ID:
                res.table = RosterappsdataColumns.TABLE_NAME;
                res.idColumn = RosterappsdataColumns._ID;
                res.tablesWithJoins = RosterappsdataColumns.TABLE_NAME;
                res.orderBy = RosterappsdataColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_SHIFT:
            case URI_TYPE_SHIFT_ID:
                res.table = ShiftColumns.TABLE_NAME;
                res.idColumn = ShiftColumns._ID;
                res.tablesWithJoins = ShiftColumns.TABLE_NAME;
                res.orderBy = ShiftColumns.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_ROSTERAPPSDATA_ID:
            case URI_TYPE_SHIFT_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = res.table + "." + res.idColumn + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = res.table + "." + res.idColumn + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }
}
