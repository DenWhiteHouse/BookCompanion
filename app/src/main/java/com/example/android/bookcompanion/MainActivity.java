package com.example.android.bookcompanion;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_library:
                Toast.makeText(this, "My Library", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
