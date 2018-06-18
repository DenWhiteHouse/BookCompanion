package com.example.android.bookcompanion.room;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;


@Entity
public class ReadingTrack {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "bookTitle")
    private String bookTitle;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "pagesRead")
    private int pagesRead;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String title) {
        this.bookTitle = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPagesRead() {
        return pagesRead;
    }

    public void setPagesRead(int pages) {
        this.pagesRead = pages;
    }
}
