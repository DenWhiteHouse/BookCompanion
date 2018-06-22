package com.example.android.bookcompanion.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = {ReadingTrack.class}, version = 3)
@TypeConverters(ReadingTrackTypeConverter.class)
public abstract class ReadingTrackDatabase extends RoomDatabase {
    private static final String DB_NAME = "readingTrackDatabase.db";
    private static volatile ReadingTrackDatabase instance;

    public static synchronized ReadingTrackDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static ReadingTrackDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                ReadingTrackDatabase.class,
                DB_NAME).fallbackToDestructiveMigration().build();
        // DB updates were just learning improvements that's why Migration is Destroyed
    }

    public abstract ReadingTrackDAO getReadingtrackDao();

}
