package com.example.android.bookcompanion;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddReadingTrack extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.BookTitleReadingtrack) EditText mBookTitle;
    @BindView(R.id.LocationReadingtrack) EditText mLocation;
    @BindView(R.id.DateReadingTrack) EditText mDate;
    @BindView(R.id.PagesReadingTrack) EditText mPages;
    @BindView(R.id.saveReadingTrackButton) Button mSaveReadingTrackButton;
    @BindView(R.id.LocationReadingtrackButton) Button mLocationButton;
    ReadingTrackDatabase readingTrackDatabase;
    ReadingTrack mReadingTrack;
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog ;
    private GoogleApiClient mApiClient;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;
    public static final String TAG = MainActivity.class.getSimpleName();

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
        setApiClient();


        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(hasWindowFocus()){
                    datePickerDialog.show();
                }
                view.clearFocus();
            }
        });

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestLocationPermission();
                placePicking();
            }
        });

        mSaveReadingTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReadingTrack.setBookTitle(mBookTitle.getText().toString());
                mReadingTrack.setLocation(mLocation.getText().toString());
                if(mPages.getText().toString().equals("")){
                    //Set value to 0 is the User didn't submit any page
                    mPages.setText("0");
                }
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

    private void setApiClient(){
                 mApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();
    }

    public void RequestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(TAG, "API Client Connection Failed!");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "API Client Connection Suspended!");
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        Log.i(TAG, "API Client Connection Successful!");
    }

    //CODE From PLACES Lesson from Udacity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            if (place == null) {
                Log.i(TAG, "No place selected");
                return;
            }

            // Extract the place information from the API
            mLocation.setText(place.getAddress().toString());
        }
    }

    private void placePicking(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), getString(R.string.location_permission_notgranted), Toast.LENGTH_LONG).show();
            return;
        }
        try {
            // Start a new Activity for the Place Picker API, this will trigger {@code #onActivityResult}
            // when a place is selected or with the user cancels.
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(this);
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }
    }

}
