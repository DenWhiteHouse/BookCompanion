package com.example.android.bookcompanion.BookQuoteFeature;

import android.app.LoaderManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookcompanion.AppExecutors;
import com.example.android.bookcompanion.BookCursorAdapter;
import com.example.android.bookcompanion.R;
import com.example.android.bookcompanion.database.BookContract;
import com.example.android.bookcompanion.widget.WidgetService;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

public class AddQuoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.bookTitle_quotespinner) Spinner bookTitleSpinner;
    @BindView(R.id.quoteContent) EditText mQuoteContent;
    @BindView(R.id.saveQuotekButton) Button mSaveQuoteButton;
    @BindView(R.id.fixedTitleforUpdate) TextView mFixedTitleForUpdateView;
    // intent labels
    public static final String EXTRA_QUOTE_ID = "extraQuoteId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_QUOTE_ID = "instanceQuoteId";
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_QUOTE_ID = -1;
    // Constant for default task id to be used when not in update mode
    private static  Boolean ON_UPDATEMODE =false;
    public Boolean mQuotehasChanged = false;
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
                AddQuoteViewModelFactory factory = new AddQuoteViewModelFactory(mDb, mQuoteID);
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
        final String quoteContent = mQuoteContent.getText().toString();
        final String quoteTitle;
        if(!ON_UPDATEMODE){
            quoteTitle = bookTitleSpinner.getSelectedItem().toString();
        }
        else{
            quoteTitle = mFixedTitleForUpdateView.getText().toString();
        }

        final QuoteEntry quote = new QuoteEntry(quoteTitle,quoteContent);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mQuoteID == DEFAULT_QUOTE_ID) {
                        mDb.QuoteDao().insertQuote(quote);
                        updateWidget(getApplicationContext(),quoteTitle +":" +"\n" + quoteContent);
                } else {
                        quote.setId(mQuoteID);
                        mDb.QuoteDao().updateQuote(quote);
                }
            }
        });
        Toast.makeText(getApplicationContext(),R.string.quoteSaved,Toast.LENGTH_SHORT).show();
        finish();
    }

    private void populateUI(QuoteEntry quote) {
        if (quote == null) {
            return;
        }
        mQuoteContent.setText(quote.getQuoteContent());
        //MAKE INVISIBILE THE SPINNER AND FIX THE TITLE
        bookTitleSpinner.setVisibility(View.GONE);
        ON_UPDATEMODE=true;
        mFixedTitleForUpdateView.setText(quote.getBookTitle());
        mFixedTitleForUpdateView.setVisibility(View.VISIBLE);
        // Add a text Watche to check for changes
        mQuoteContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mQuotehasChanged=true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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
        if((bookTitleSpinner.getSelectedItemPosition())==0 && !ON_UPDATEMODE){
            Toast.makeText(this,R.string.warning_empty_title,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mQuoteContent.getText().toString().isEmpty()){
            Toast.makeText(this,R.string.quoteContent_empty,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //Check if the User has made changes and alert the user before leaving the activity
    @Override
    public void onBackPressed() {
        if (!mQuotehasChanged) {
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

    public void updateWidget(Context context, String quote){
        //Send and Intent to WidgetService
        WidgetService.widgetIntent(context, quote);
    }
}

