package com.example.todos.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class TodosContract {
    //URI section
    public static final String CONTENT_AUTHORITY = "com.example.todos.todosprovider";
    public static final String PATH_TODOS="todos";
    public static final String PATH_CATEGORIES="categories";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public String concatContent(String path){
        return "content://" + path;
    }

    public static final class TodosEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TODOS);

        // Table name
        public static final String TABLE_NAME = "todos";
        //column (field) names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_CREATED = "created";
        public static final String COLUMN_EXPIRED = "expired";
        public static final String COLUMN_DONE = "done";
        public static final String COLUMN_CATEGORY = "category";
    }

    public static final class CategoriesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CATEGORIES);

        // Table name
        public static final String TABLE_NAME = "categories";
        //column names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DESCRIPTION = "description";
    }
}
