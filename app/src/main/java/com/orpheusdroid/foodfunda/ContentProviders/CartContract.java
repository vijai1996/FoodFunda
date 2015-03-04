/*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 % Copyright (c) 2015. Vijai Chandra Prasad R.                                                    %
 %                                                                                                %
 % This program is free software: you can redistribute it and/or modify                           %
 % it under the terms of the GNU General Public License as published by                           %
 % the Free Software Foundation, either version 3 of the License, or                              %
 % (at your option) any later version.                                                            %
 %                                                                                                %
 % This program is distributed in the hope that it will be useful,                                %
 % but WITHOUT ANY WARRANTY; without even the implied warranty of                                 %
 % MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                                  %
 % GNU General Public License for more details.                                                   %
 %                                                                                                %
 % You should have received a copy of the GNU General Public License                              %
 % along with this program.  If not, see http://www.gnu.org/licenses                              %
 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/

package com.orpheusdroid.foodfunda.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.orpheusdroid.foodfunda.utility.Debug;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by vijai on 25-02-2015.
 */
public class CartContract extends ContentProvider {
    public static final String TABLE_NAME = "Cart";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ITEM = "item";
    public static final String COLUMN_ITEM_QUANTITY = "quantity";
    public static final String COLUMN_ITEM_PRICE = "price";

    // database
    private Dbhelper database;

    // used for the UriMacher
    private static final int CART = 10;
    private static final int CART_ID = 20;

    private static final String AUTHORITY = "com.orpheusdroid.foodfunda.ContentProviders";

    private static final String BASE_PATH = "cart";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/cart";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/cart";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, CART);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CART_ID);
    }

    @Override
    public boolean onCreate() {
        database = new Dbhelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case CART:
                break;
            case CART_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case CART:
                id = sqlDB.insert(TABLE_NAME, null, values);
                Debug.d("DB", "Inserted into db: " + uriType);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case CART:
                rowsDeleted = sqlDB.delete(TABLE_NAME, selection,
                        selectionArgs);
                break;
            case CART_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(TABLE_NAME,
                            COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(TABLE_NAME,
                            COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case CART:
                rowsUpdated = sqlDB.update(TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CART_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(TABLE_NAME,
                            values,
                            COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(TABLE_NAME,
                            values,
                            COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { COLUMN_ITEM,
                COLUMN_ITEM_QUANTITY, COLUMN_ITEM_PRICE,
                COLUMN_ID };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}

