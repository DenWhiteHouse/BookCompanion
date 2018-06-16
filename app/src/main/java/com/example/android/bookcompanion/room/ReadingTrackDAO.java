package com.example.android.bookcompanion.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ReadingTrackDAO {

    @Query("SELECT * FROM readingtrack")
    List<ReadingTrack> getAll();

    @Query("SELECT * FROM readingtrack WHERE bookTitle LIKE :booktitle")
    ReadingTrack findByName(String booktitle);

    @Insert
    void insertAll(List<ReadingTrack> readingTracks);

    @Update
    void update(ReadingTrack readingTrack);

    @Delete
    void delete(ReadingTrack readingTrack);
}
