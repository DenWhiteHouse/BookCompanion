package com.example.android.bookcompanion.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class BookContract {
    private BookContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.bookcompanion.books";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "books";

    public static final class BookEntry implements BaseColumns {
        //URI AND MIME
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        //SCHEMA
        public final static String TABLE_NAME = "mybooks";
        // Primary Key
        public final static String _ID = BaseColumns._ID;
        //Book Info
        public final static String COL_BOOK_NAME = "name";
        public final static String COL_BOOK_AUTH = "author";
        public final static String COL_BOOK_PAGES = "pages";
        public final static String COL_BOOK_START_DATE = "startdate";
        public final static String COL_BOOK_END_DATE= "enddate";
    }
}
