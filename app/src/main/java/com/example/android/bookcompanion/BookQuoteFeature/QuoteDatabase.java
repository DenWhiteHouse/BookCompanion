package com.example.android.bookcompanion.BookQuoteFeature;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {QuoteEntry.class}, version = 1, exportSchema = false)
public abstract class QuoteDatabase extends RoomDatabase {

    private static final String LOG_TAG = QuoteDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "quote.db";
    private static QuoteDatabase sInstance;

    public static QuoteDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        QuoteDatabase.class, QuoteDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract QuoteDao QuoteDao();

}


