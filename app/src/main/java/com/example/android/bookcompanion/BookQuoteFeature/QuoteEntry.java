package com.example.android.bookcompanion.BookQuoteFeature;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "quote") public class QuoteEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String bookTitle;
    private String quoteContent;

    public QuoteEntry(String bookTitle, String quoteContent) {
        this.bookTitle = bookTitle;
        this.quoteContent = quoteContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitlen(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getQuoteContent() {
        return quoteContent;
    }

    public void setQuoteContent(String quoteContent) {
        this.quoteContent = quoteContent;
    }
}
