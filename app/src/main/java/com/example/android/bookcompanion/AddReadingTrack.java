package com.example.android.bookcompanion;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookcompanion.data.AddBook;
import com.example.android.bookcompanion.database.BookContract;
import com.example.android.bookcompanion.room.ReadingTrack;
import com.example.android.bookcompanion.room.ReadingTrackDatabase;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    Date dateObject;
    private static SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog ;

    public AddReadingTrack(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reading_track);
        ButterKnife.bind(this);
        mReadingTrack = new ReadingTrack();
        dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        mDate.setInputType(InputType.TYPE_NULL);
        setDateTimeField();

        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(hasWindowFocus()){
                    datePickerDialog.show();
                }
                view.clearFocus();
            }
        });

        mSaveReadingTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReadingTrack.setBookTitle(mBookTitle.getText().toString());
                mReadingTrack.setLocation(mLocation.getText().toString());
                mReadingTrack.setPagesRead(Integer.parseInt(mPages.getText().toString()));

                //Simple Runnable and not executor as the User will be add only one Reading track per time.
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        readingTrackDatabase.getInstance(getApplicationContext()).getReadingtrackDao().insertReadingTrack(mReadingTrack);
                    }
                });
                t.start();
                Toast.makeText(AddReadingTrack.this, R.string.reading_track_saved, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setDateTimeField() {
        // Easy DatePicker solution from @NithyaVasudevan+
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDate.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}
