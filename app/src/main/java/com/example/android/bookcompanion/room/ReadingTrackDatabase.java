package com.example.android.bookcompanion.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;

@Database(entities = {ReadingTrack.class}, version = 1)
@TypeConverters({ReadingTrackTypeConverter.class})
public abstract class ReadingTrackDatabase extends RoomDatabase {
    public abstract ReadingTrackDAO readingtrackDao();

}
