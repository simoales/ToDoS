package com.example.todos.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.todos.data.TodosContract.CONTENT_AUTHORITY;
import static com.example.todos.data.TodosContract.CategoriesEntry;
import static com.example.todos.data.TodosContract.PATH_CATEGORIES;
import static com.example.todos.data.TodosContract.PATH_TODOS;
import static com.example.todos.data.TodosContract.TodosEntry;

public class TodosProvider extends ContentProvider{
    //constants for the operation
    private static final int TODOS = 1;
    private static final int TODOS_ID = 2;
    private static final int CATEGORIES = 3;
    private static final int CATEGORIES_ID = 4;
    //urimatcher
    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_TODOS, TODOS);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_TODOS + "/#", TODOS_ID);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_CATEGORIES, CATEGORIES);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_CATEGORIES + "/#", CATEGORIES_ID);
    }

    private DatabaseHelper helper;
    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        //get db
        SQLiteDatabase db = helper.getReadableDatabase();
        //cursor
        Cursor cursor;
        //our integer
        int match = uriMatcher.match(uri);
        //intables
        String inTables = TodosEntry.TABLE_NAME
                + " inner join "
                + CategoriesEntry.TABLE_NAME
                + " on " + TodosEntry.COLUMN_CATEGORY + " = "
                + CategoriesEntry.TABLE_NAME + "." + CategoriesEntry._ID;
        SQLiteQueryBuilder builder;
        switch (match) {
            case TODOS:
                builder = new SQLiteQueryBuilder();
                builder.setTables(inTables);
                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, orderBy);
                break;
            case TODOS_ID:
                builder = new SQLiteQueryBuilder();
                builder.setTables(inTables);
                selection = TodosEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = builder.query(db, projection, selection, selectionArgs, null, null, orderBy);
                break;
            case CATEGORIES:
                cursor = db.query(CategoriesEntry.TABLE_NAME,
                        projection, null, null, null, null, orderBy);
                break;
            case CATEGORIES_ID:
                selection = CategoriesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(CategoriesEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, orderBy);
                break;
            default:
                throw new IllegalArgumentException("Query unknown URI: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case TODOS:
                return insertRecord(uri, contentValues, TodosEntry.TABLE_NAME);
            case CATEGORIES:
                return insertRecord(uri, contentValues, CategoriesEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
    }

    private Uri insertRecord(Uri uri, ContentValues values, String table) {
        //this time we need a writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        long id = db.insert(table, null, values);
        if (id == -1) {
            Log.e("Error", "insert error for URI " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case TODOS:
                return deleteRecord(uri, null, null, TodosEntry.TABLE_NAME);
            case TODOS_ID:
                return deleteRecord(uri, selection, selectionArgs, TodosEntry.TABLE_NAME);
            case CATEGORIES:
                return deleteRecord(uri, null, null, CategoriesEntry.TABLE_NAME);
            case CATEGORIES_ID:
                long id = ContentUris.parseId(uri);
                selection = CategoriesEntry._ID + "=?";
                String[] sel = new String[1];
                sel[0] = String.valueOf(id);
                return deleteRecord(uri, selection, sel,
                        CategoriesEntry.TABLE_NAME);

            default:
                throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
    }

    private int deleteRecord(Uri uri, String selection, String[] selectionArgs, String tableName) {
        //this time we need a writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        int id = db.delete(tableName, selection, selectionArgs);
        if (id == -1) {
            Log.e("Error", "delete unknown URI " + uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case TODOS:
                return updateRecord(uri, values, selection, selectionArgs, TodosEntry.TABLE_NAME);
            case CATEGORIES:
                return updateRecord(uri, values, selection, selectionArgs, CategoriesEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Update unknown URI: " + uri);
        }
    }

    private int updateRecord(Uri uri, ContentValues values, String selection, String[] selectionArgs, String tableName) {
        //this time we need a writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        int id = db.update(tableName, values, selection, selectionArgs);
        if (id == 0) {
            Log.e("Error", "update error for URI " + uri);
            return -1;
        }
        return id;
    }
}
