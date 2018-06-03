package com.example.android.bookcompanion.data;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookcompanion.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBook extends AppCompatActivity {
    private String mBookTitle;
    Book books;
    @BindView(R.id.bookTitleInput) EditText mEditText;
    @BindView(R.id.search_book_button) Button mSearchButton;
    @BindView(R.id.BookResultsView) LinearLayout resultsView;
    @BindView(R.id.bookTitle) TextView mBookTitleTV;
    @BindView(R.id.bookAuthor) TextView mBookAuthorTV;
    @BindView(R.id.bookPages) TextView mBookPageTV;
    @BindView(R.id.bookImage) ImageView mBookImage;

    public AddBook(){
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO: 03/06/2018 BUNDLE FOR BOOKS RESULTS 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);
        ButterKnife.bind(this);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBookTitle=  mEditText.getText().toString();
                if (mBookTitle.isEmpty()) {
                    Toast.makeText(AddBook.this, R.string.warning_missing_title, Toast.LENGTH_SHORT).show();
                }
                else{
                    getBookJSON();
                }
            }
        });
    }

    public void getBookJSON(){
        //Getting the JSON Object with Retrofit
        BooksInterface booksInterface = RetrofitClient.getRetrofitInstance();
        Call<Book> book = booksInterface.getBooks(mBookTitle);

        book.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    books = response.body();
                    if(books.getItems() != null) {
                        setBookInfo();
                        resultsView.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(AddBook.this, R.string.warning_title_notFound, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(AddBook.this, R.string.warning_title_notFound, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Toast.makeText(AddBook.this, R.string.error_Retrofit, Toast.LENGTH_SHORT).show();
                Log.v("Retrofit has failed ", t.getMessage());
            }
        });
    }

    public void setBookInfo() {
        //This version will only use the first item result of the API
        String checkString;

        checkString = books.getItems().get(0).getVolumeInfo().getTitle();
        if (!checkString.isEmpty()) {
            mBookTitleTV.setText(checkString);
        }
        if (books.getItems().get(0).getVolumeInfo().getAuthors() != null){
        checkString = books.getItems().get(0).getVolumeInfo().getAuthors().get(0);
        if (!checkString.isEmpty()) {
            mBookAuthorTV.setText(checkString);
        }
    }
        checkString=String.valueOf(books.getItems().get(0).getVolumeInfo().getPageCount());
        if(!checkString.isEmpty()) {
            mBookPageTV.setText(checkString);
        }
        if (books.getItems().get(0).getVolumeInfo().getImageLinks() != null){
        String imageUrl = books.getItems().get(0).getVolumeInfo().getImageLinks().getThumbnail();
        if(!imageUrl.isEmpty()) {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(getApplicationContext()).load(builtUri).into(mBookImage);
        }
    }
    }
}
