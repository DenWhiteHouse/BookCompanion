package com.example.android.bookcompanion;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditReadingTrack extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    // I have divided EDIT from ADD without using a FLAG for learning purposes
@BindView(R.id.LocationReadingtrack) EditText mLocation;
@BindView(R.id.DateReadingTrack)  EditText mDate;
@BindView(R.id.PagesReadingTrack) EditText mPages;
@BindView(R.id.updateReadingTrackButton)  Button mEditReadingTrackButton;
@BindView(R.id.LocationReadingtrackButton) Button mLocationButton;
@BindView(R.id.bookTitle_editview) TextView bookTitle;
    private GoogleApiClient mApiClient;
    ReadingTrackDatabase readingTrackDatabase;
    ReadingTrack mReadingTrack;
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog ;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;
    public static final String TAG = EditReadingTrack.class.getSimpleName();
    public Boolean mTrackhasChanged = false;


    public EditReadingTrack(){
    }

    //Listener for EditView changes
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mEditReadingTrackButton.setVisibility(View.VISIBLE);
            mTrackhasChanged =true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reading_track);
        ButterKnife.bind(this);
        //Setting the page features
        setButtons();
        setApiClient();
        mReadingTrack = new ReadingTrack();
        dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        setDateTimeField();
        // Get Track information from intent and populate the UI
        Intent intent = getIntent();
        bookTitle.setText(intent.getStringExtra("BOOKTITLE"));
        mLocation.setText(intent.getStringExtra("BOOKLOCATION"));
        mDate.setText(intent.getStringExtra("BOOKDATE"));
        mPages.setText(intent.getStringExtra("BOOKPAGES"));
        mReadingTrack.setUid(intent.getIntExtra("ID",-1));
        // Setting onChangeHelper

        mLocation.setOnTouchListener(mTouchListener);
        mDate.setOnTouchListener(mTouchListener);
        mPages.setOnTouchListener(mTouchListener);
    }

    // CLASS METHODS

    public void setButtons(){
        mEditReadingTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInputFields()) {
                    mReadingTrack.setBookTitle(bookTitle.getText().toString());
                    mReadingTrack.setLocation(mLocation.getText().toString());
                    mReadingTrack.setPagesRead(Integer.parseInt(mPages.getText().toString()));
                    mReadingTrack.setDate(mDate.getText().toString());

                    //Simple Runnable and not executor as the User will be add only one Reading track per time.
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            readingTrackDatabase.getInstance(getApplicationContext()).getReadingtrackDao().update(mReadingTrack);
                        }
                    });
                    t.start();
                    Toast.makeText(EditReadingTrack.this, R.string.itemUpdated, Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

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

    public void RequestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_FINE_LOCATION);
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

    //Check if the User has made changes and alert the user before leaving the activity
    @Override
    public void onBackPressed() {
        if (!mTrackhasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the fruit.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean checkInputFields(){
        if(mLocation.getText().toString().isEmpty()){
            Toast.makeText(this,R.string.warning_empty_location,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mDate.getText().toString().isEmpty()){
            Toast.makeText(this,R.string.warning_empty_title,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mPages.getText().toString().isEmpty()){
            Toast.makeText(this,R.string.warning_empty_title,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
