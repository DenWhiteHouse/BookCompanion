package com.example.android.bookcompanion;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookcompanion.data.AddBook;
import com.example.android.bookcompanion.database.BookContract;
import com.example.android.bookcompanion.room.ReadingTrack;
import com.example.android.bookcompanion.room.ReadingTrackDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddReadingTrack extends AppCompatActivity {
    @BindView(R.id.BookTitleReadingtrack) EditText mBookTitle;
    @BindView(R.id.LocationReadingtrack) EditText mLocation;
    @BindView(R.id.DateReadingTrack) EditText mDate;
    @BindView(R.id.PagesReadingTrack) EditText mPages;
    @BindView(R.id.saveReadingTrackButton) Button mSaveReadingTrackButton;
    ReadingTrackDatabase readingTrackDatabase;
    ReadingTrack mReadingTrack;

    public AddReadingTrack(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reading_track);
        ButterKnife.bind(this);
        mReadingTrack = new ReadingTrack();

        mSaveReadingTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReadingTrack.setBookTitle(mBookTitle.getText().toString());
                mReadingTrack.setDate(mDate.getText().toString());
                mReadingTrack.setLocation(mLocation.getText().toString());
                mReadingTrack.setPagesRead(Integer.parseInt(mPages.getText().toString()));
               // readingTrackDatabase.getInstance(getApplicationContext()).getReadingtrackDao().insertReadingTrack(mReadingTrack);
                Toast.makeText(AddReadingTrack.this, R.string.reading_track_saved, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
