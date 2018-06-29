package com.example.android.bookcompanion.data;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.android.bookcompanion.database.BookContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBook extends AppCompatActivity {
    Book books;
    @BindView(R.id.bookTitleInput)
    EditText mEditText;
    @BindView(R.id.search_book_button)
    Button mSearchButton;
    @BindView(R.id.BookResultsView)
    LinearLayout resultsView;
    @BindView(R.id.bookTitle)
    TextView mBookTitleTV;
    @BindView(R.id.bookAuthor)
    TextView mBookAuthorTV;
    @BindView(R.id.bookPages)
    TextView mBookPageTV;
    @BindView(R.id.bookImage)
    ImageView mBookImage;
    @BindView(R.id.saveBookButton)
    Button mSaveBookButton;
    private String mBookTitle;
    private String mImage, mTitle, mAuthor, mPages;
    private String bookbundle = "book";

    public AddBook() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            books = savedInstanceState.getParcelable(bookbundle);
            if (books != null) {
                setBookInfo();
                resultsView.setVisibility(View.VISIBLE);
            }
        }

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBookTitle = mEditText.getText().toString();
                if (mBookTitle.isEmpty()) {
                    Toast.makeText(AddBook.this, R.string.warning_missing_title, Toast.LENGTH_SHORT).show();
                } else {
                    getBookJSON();
                }
            }
        });

        mSaveBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(BookContract.BookEntry.COL_BOOK_IMAGE, mImage);
                values.put(BookContract.BookEntry.COL_BOOK_NAME, mTitle);
                values.put(BookContract.BookEntry.COL_BOOK_AUTH, mAuthor);
                values.put(BookContract.BookEntry.COL_BOOK_PAGES, mPages);
                Uri newUri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);
                if (newUri == null) {
                    Toast.makeText(AddBook.this, R.string.notUniqueBook, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddBook.this, R.string.book_saved, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    public void getBookJSON() {
        //Getting the JSON Object with Retrofit
        BooksInterface booksInterface = RetrofitClient.getRetrofitInstance();
        Call<Book> book = booksInterface.getBooks(mBookTitle);

        book.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    books = response.body();
                    if (books.getItems() != null) {
                        setBookInfo();
                        resultsView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(AddBook.this, R.string.warning_title_notFound, Toast.LENGTH_SHORT).show();
                    }
                } else {
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

        mTitle = books.getItems().get(0).getVolumeInfo().getTitle();
        if (!mTitle.isEmpty()) {
            mBookTitleTV.setText(mTitle);
        }
        if (books.getItems().get(0).getVolumeInfo().getAuthors() != null) {
            mAuthor = books.getItems().get(0).getVolumeInfo().getAuthors().get(0);
            if (!mAuthor.isEmpty()) {
                mBookAuthorTV.setText(mAuthor);
            }
        }
        mPages = String.valueOf(books.getItems().get(0).getVolumeInfo().getPageCount());
        if (!mPages.isEmpty()) {
            mBookPageTV.setText(mPages);
        }
        if (books.getItems().get(0).getVolumeInfo().getImageLinks() != null) {
            mImage = books.getItems().get(0).getVolumeInfo().getImageLinks().getThumbnail();
            if (!mImage.isEmpty()) {
                Uri builtUri = Uri.parse(mImage).buildUpon().build();
                Picasso.with(getApplicationContext()).load(builtUri).into(mBookImage);
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Check if the user has already used the research function
        if (books != null) {
            outState.putParcelable(bookbundle, books);
            super.onSaveInstanceState(outState);
        }
    }
}
