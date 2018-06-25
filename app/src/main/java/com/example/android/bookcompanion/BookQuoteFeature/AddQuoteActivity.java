package com.example.android.bookcompanion.BookQuoteFeature;

import android.app.LoaderManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.bookcompanion.AppExecutors;
import com.example.android.bookcompanion.BookCursorAdapter;
import com.example.android.bookcompanion.R;
import com.example.android.bookcompanion.database.BookContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddQuoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.bookTitle_quotespinner) Spinner bookTitleSpinner;
    @BindView(R.id.quoteContent) EditText mQuoteContent;
    @BindView(R.id.saveQuotekButton) Button mSaveQuoteButton;
    // Extra for the task ID to be received in the intent
    public static final String EXTRA_QUOTE_ID = "extraQuoteId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_QUOTE_ID = "instanceQuoteId";
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_QUOTE_ID = -1;
    // Constant for logging
    private static final String TAG = AddQuoteActivity.class.getSimpleName();
    private int mQuoteID = DEFAULT_QUOTE_ID;
    // Member variable for the Database
    private QuoteDatabase mDb;
    //Getting the Book Titles
    BookCursorAdapter mCursorAdapter;
    private static final int BOOK_LOADER = 0;
    Cursor mBookCursor;
    String[] mBookTitleSpinnerArray;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_quote);
        ButterKnife.bind(this);

        //Managing DB for Spinner Title
        mCursorAdapter = new BookCursorAdapter(getApplicationContext(), null);
        getLoaderManager().initLoader(BOOK_LOADER, null, this);

        mDb = QuoteDatabase.getInstance(getApplicationContext());
        //Manage Control is the Activity is in ADD or UPDATE MODE
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_QUOTE_ID)) {
            mQuoteID = savedInstanceState.getInt(INSTANCE_QUOTE_ID, DEFAULT_QUOTE_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_QUOTE_ID)) {
            mSaveQuoteButton.setText(R.string.updateQuote);
            if (mQuoteID == DEFAULT_QUOTE_ID) {
                // populate the UI
                mQuoteID = intent.getIntExtra(EXTRA_QUOTE_ID, DEFAULT_QUOTE_ID);
                AddQuoteViewModelFactory factory = new AddQuoteViewModelFactory(mDb, intent.getStringExtra("BOOKTITLE"));
                final AddQuoteViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddQuoteViewModel.class);
                viewModel.getQuote().observe(this, new Observer<QuoteEntry>() {
                    @Override
                    public void onChanged(@Nullable QuoteEntry quoteEntry) {
                        viewModel.getQuote().removeObserver(this);
                        populateUI(quoteEntry);
                    }
                });
            }
        }

        mSaveQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInputFields()) {
                    onSaveButtonClicked();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_QUOTE_ID, mQuoteID);
        super.onSaveInstanceState(outState);
    }

    public void onSaveButtonClicked() {
        String quoteContent = mQuoteContent.getText().toString();
        String quoteTitle = bookTitleSpinner.getSelectedItem().toString();

        final QuoteEntry quote = new QuoteEntry(quoteTitle,quoteContent);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mQuoteID == DEFAULT_QUOTE_ID) {
                        mDb.QuoteDao().insertQuote(quote);
                } else {
                        quote.setId(mQuoteID);
                        mDb.QuoteDao().updateQuote(quote);
                }
                finish();
            }
        });
    }

    private void populateUI(QuoteEntry quote) {
        if (quote == null) {
            return;
        }
        mQuoteContent.setText(quote.getQuoteContent());
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

    private String[] setSpinnerTitleArray(){
        ArrayList<String> mBookTitleArray = new ArrayList<String>();
        mBookTitleArray.add(getResources().getString(R.string.selectTitle_Spinner));
        mBookCursor.moveToFirst();
        if(mBookCursor.isAfterLast()){
            //The Library is empty and disable saving button
            mBookTitleArray.add(getResources().getString(R.string.selectTitle_Spinner));
            mSaveQuoteButton.setClickable(false);
        }
        else {
            while (!mBookCursor.isAfterLast()){
                mBookTitleArray.add(mBookCursor.getString(mBookCursor.getColumnIndex("name")));
                mBookCursor.moveToNext();
            }
        }
        return mBookTitleArray.toArray(new String[mBookTitleArray.size()]);
    }

    private boolean checkInputFields(){
        if((bookTitleSpinner.getSelectedItemPosition())==0){
            Toast.makeText(this,R.string.warning_empty_title,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mQuoteContent.getText().toString().isEmpty()){
            Toast.makeText(this,R.string.quoteContent_empty,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}

