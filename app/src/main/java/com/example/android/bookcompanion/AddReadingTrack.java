package com.example.android.bookcompanion;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddReadingTrack extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.LocationReadingtrack) EditText mLocation;
    @BindView(R.id.DateReadingTrack) EditText mDate;
    @BindView(R.id.PagesReadingTrack) EditText mPages;
    @BindView(R.id.saveReadingTrackButton) Button mSaveReadingTrackButton;
    @BindView(R.id.LocationReadingtrackButton) Button mLocationButton;
    @BindView(R.id.bookTitle_spinner) Spinner bookTitleSpinner;
    ReadingTrackDatabase readingTrackDatabase;
    ReadingTrack mReadingTrack;
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog ;
    private GoogleApiClient mApiClient;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;
    public static final String TAG = MainActivity.class.getSimpleName();
    BookCursorAdapter mCursorAdapter;
    private static final int BOOK_LOADER = 0;
    Cursor mBookCursor;
    String[] mBookTitleSpinnerArray;


    public AddReadingTrack(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reading_track);
        ButterKnife.bind(this);

        //Managing DB for Spinner Title
        mCursorAdapter = new BookCursorAdapter(getApplicationContext(), null);
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
        //Setting Date Picker
        mReadingTrack = new ReadingTrack();
        dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        setDateTimeField();
        //Setting Place Picker
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
                if (checkInputFields()) {
                    mReadingTrack.setBookTitle(bookTitleSpinner.getSelectedItem().toString());
                    mReadingTrack.setLocation(mLocation.getText().toString());
                    mReadingTrack.setPagesRead(Integer.parseInt(mPages.getText().toString()));
                    mReadingTrack.setDate(mDate.getText().toString());

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

    private boolean checkInputFields(){
        if((bookTitleSpinner.getSelectedItemPosition())==0){
            Toast.makeText(this,R.string.warning_empty_title,Toast.LENGTH_SHORT).show();
            return false;
        }
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

    private String[] setSpinnerTitleArray(){
        ArrayList<String> mBookTitleArray = new ArrayList<String>();
        mBookTitleArray.add(getResources().getString(R.string.selectTitle_Spinner));
        mBookCursor.moveToFirst();
        if(mBookCursor.isAfterLast()){
            //The Library is empty and disable saving button
            mBookTitleArray.add(getResources().getString(R.string.selectTitle_Spinner));
            mSaveReadingTrackButton.setClickable(false);
        }
        else {
            while (!mBookCursor.isAfterLast()){
                mBookTitleArray.add(mBookCursor.getString(mBookCursor.getColumnIndex("name")));
                mBookCursor.moveToNext();
            }
        }
        return mBookTitleArray.toArray(new String[mBookTitleArray.size()]);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COL_BOOK_IMAGE,
                BookContract.BookEntry.COL_BOOK_NAME, BookContract.BookEntry.COL_BOOK_AUTH,
                BookContract.BookEntry.COL_BOOK_PAGES, BookContract.BookEntry.COL_BOOK_START_DATE, BookContract.BookEntry.COL_BOOK_START_DATE};

        return new CursorLoader(this,
                BookContract.BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
       mBookCursor = mCursorAdapter.getCursor();
       mBookTitleSpinnerArray =  setSpinnerTitleArray();
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,mBookTitleSpinnerArray);
        stringArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bookTitleSpinner.setAdapter(stringArrayAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}


