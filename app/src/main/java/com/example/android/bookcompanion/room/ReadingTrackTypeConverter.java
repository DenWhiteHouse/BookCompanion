package com.example.android.bookcompanion.room;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class ReadingTrackTypeConverter {

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toTimeStamp(Date value) {
        return value == null ? null : value.getTime();
    }
}

