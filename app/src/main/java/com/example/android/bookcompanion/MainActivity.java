package com.example.android.bookcompanion;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.bookcompanion.BookQuoteFeature.AddQuoteActivity;
import com.example.android.bookcompanion.data.AddBook;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {
    FloatingActionButton mAddBookFAB,mAddTrackFAB,mAddQuoteFAB;
    private static final int BOOK_LOADER = 0;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Inizialize AdTest
        MobileAds.initialize(this, getString(R.string.idclient));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);





        mAddBookFAB =findViewById(R.id.AddNewBookFAB);
        mAddTrackFAB =findViewById(R.id.AddNewTrackFAB);
        mAddQuoteFAB =findViewById(R.id.AddNewQuoteFAB);

        mAddBookFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddBook.class));
            }
        });

        mAddTrackFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddReadingTrack.class));
            }
        });

        mAddQuoteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddQuoteActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_library:
                startActivity(new Intent(this,MyLibrary.class));
                return true;
            case R.id.my_reading_tracks:
                startActivity(new Intent(this,MyReadingTracks.class));
                return true;
            case R.id.my_quotes:
                startActivity(new Intent(this,MyQuotes.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
