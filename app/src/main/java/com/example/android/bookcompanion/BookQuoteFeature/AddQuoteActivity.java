package com.example.android.bookcompanion.BookQuoteFeature;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.bookcompanion.AppExecutors;
import com.example.android.bookcompanion.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddQuoteActivity extends AppCompatActivity {
    @BindView(R.id.bookTitle_quotespinner) Spinner mTitleSpinner;
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
    //todo: bind the views

    private int mQuoteID = DEFAULT_QUOTE_ID;
    // Member variable for the Database
    private QuoteDatabase mDb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_quote);
        ButterKnife.bind(this);

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
                AddQuoteViewModelFactory factory = new AddQuoteViewModelFactory(mDb, "BookTitle");
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
                onSaveButtonClicked();
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

        final QuoteEntry quote = new QuoteEntry("prova",quoteContent);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mQuoteID == DEFAULT_QUOTE_ID) {
                    // insert new task
                    mDb.QuoteDao().insertQuote(quote);
                } else {
                    //update task
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
}
