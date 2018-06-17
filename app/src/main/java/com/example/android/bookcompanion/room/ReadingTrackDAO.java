package com.example.android.bookcompanion.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
@TypeConverters(ReadingTrackTypeConverter.class)
public interface ReadingTrackDAO {

    @Query("SELECT * FROM readingtrack")
    LiveData<List<ReadingTrack>> getAllReadingTrack();

    @Query("SELECT * FROM readingtrack WHERE bookTitle LIKE :booktitle")
    ReadingTrack findByName(String booktitle);

    @Insert
    void insertAll(List<ReadingTrack> readingTracks);

    @Insert
    void insertReadingTrack(ReadingTrack readingTracks);

    @Update
    void update(ReadingTrack readingTrack);

    @Delete
    void delete(ReadingTrack readingTrack);
}
