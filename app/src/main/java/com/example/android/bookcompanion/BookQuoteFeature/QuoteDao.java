package com.example.android.bookcompanion.BookQuoteFeature;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface QuoteDao {

    @Query("SELECT * FROM quote")
    LiveData<List<QuoteEntry>> loadAllQuotes();

    @Query("SELECT * FROM quote WHERE bookTitle = :bookTitle")
    LiveData<List<QuoteEntry>> loadQuoteByTitle(String bookTitle);

    @Query("SELECT * FROM quote WHERE id = :id")
    LiveData<QuoteEntry> loadSingleQuoteByID(int id);

    @Insert
    void insertQuote(QuoteEntry quoteEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateQuote(QuoteEntry taskEntry);

    @Delete
    void deleteQuote(QuoteEntry quoteEntry);

    @Query("DELETE FROM quote WHERE bookTitle = :bookTitle")
    abstract void deleteQuotesByBookTitle(String bookTitle);

}
