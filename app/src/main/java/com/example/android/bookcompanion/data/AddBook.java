package com.example.android.bookcompanion.data;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookcompanion.R;

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

    public AddBook(){
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        Call<Book> book = booksInterface.getBooks();

        book.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                books = response.body();
                setBookInfo();
                resultsView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.v("Retrofit has failed ", t.getMessage());
            }
        });
    }

    public void setBookInfo(){
        //This version will only use the first risult of the API
        mBookTitleTV.setText(books.getItems().get(0).getVolumeInfo().getTitle());
        mBookAuthorTV.setText(books.getItems().get(0).getVolumeInfo().getAuthors().get(0));
        mBookPageTV.setText(String.valueOf(books.getItems().get(0).getVolumeInfo().getPageCount()));

    }
}
