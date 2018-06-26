package com.example.android.bookcompanion.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = BookDBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "mylibrary.db";
    private static final int DATABASE_VERSION = 1;

    public BookDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the fruits table
        String SQL_CREATE_FRUITS_TABLE = "CREATE TABLE " + BookContract.BookEntry.TABLE_NAME + " ("
                + BookContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookContract.BookEntry.COL_BOOK_IMAGE + " TEXT NOT NULL, "
                + BookContract.BookEntry.COL_BOOK_NAME + " TEXT NOT NULL UNIQUE, "
                + BookContract.BookEntry.COL_BOOK_AUTH + " TEXT NOT NULL, "
                + BookContract.BookEntry.COL_BOOK_PAGES + " INTEGER NOT NULL, "
                + BookContract.BookEntry.COL_BOOK_START_DATE + " INTEGER, "
                + BookContract.BookEntry.COL_BOOK_END_DATE + " INTEGER );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_FRUITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed and create
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}
