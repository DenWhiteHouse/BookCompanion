package com.example.android.bookcompanion;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.bookcompanion.data.AddBook;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton mAddBookFAB,mAddTrackFAB,mAddQuoteFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddBookFAB =findViewById(R.id.AddNewBookFAB);
        mAddTrackFAB =findViewById(R.id.AddNewTrackFAB);
        mAddQuoteFAB =findViewById(R.id.AddNewQuoteFAB);

        mAddBookFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddBook.class));
            }
        });
    }
}
