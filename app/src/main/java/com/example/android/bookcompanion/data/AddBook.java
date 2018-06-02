package com.example.android.bookcompanion.data;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.bookcompanion.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddBook extends AppCompatActivity {
    @BindView(R.id.bookTitleInput) EditText mEditText;
    String mBookTitle;
    Button mSearchButton;

    public AddBook(){
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);
        ButterKnife.bind(this);
        mSearchButton=findViewById(R.id.search_book_button);
        final LinearLayout resultsView= findViewById(R.id.BookResultsView);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBookTitle=  mEditText.getText().toString();
                if (mBookTitle.isEmpty()) {
                    Toast.makeText(AddBook.this, R.string.warning_missing_title, Toast.LENGTH_SHORT).show();
                }
                else{
                    resultsView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
