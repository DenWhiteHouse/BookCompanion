package com.example.android.bookcompanion;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bookcompanion.BookQuoteFeature.MyQuotesViewModel;
import com.example.android.bookcompanion.BookQuoteFeature.QuoteAdapter;
import com.example.android.bookcompanion.BookQuoteFeature.QuoteDatabase;
import com.example.android.bookcompanion.BookQuoteFeature.QuoteEntry;

import java.util.List;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class MyQuotes extends AppCompatActivity{


    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private QuoteAdapter mAdapter;
    private QuoteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_quote_activity);

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.quote_RecyclerView);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(decoration);

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new QuoteAdapter(this);
        mRecyclerView.setAdapter(mAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyQuotes.this);
                builder.setMessage(R.string.swipeAlert);
                builder.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                int position = viewHolder.getAdapterPosition();
                                List<QuoteEntry> quotes = mAdapter.getQuotes();
                                mDb.QuoteDao().deleteQuote(quotes.get(position));
                            }
                        });
                        dialog.cancel();
                        Toast.makeText(MyQuotes.this, "item Deleted ", Toast.LENGTH_SHORT).show();
                    }
                });
                        builder.setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                dialog.cancel();
                            };
                        });
                builder.show();
            }
        }).attachToRecyclerView(mRecyclerView);

        mDb = QuoteDatabase.getInstance(getApplicationContext());
        setupViewModel();
    }

    private void setupViewModel() {
        MyQuotesViewModel viewModel = ViewModelProviders.of(this).get(MyQuotesViewModel.class);
        viewModel.getQuotes().observe(this, new Observer<List<QuoteEntry>>() {
            @Override
            public void onChanged(@Nullable List<QuoteEntry> quoteEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mAdapter.setQuotes(quoteEntries);
                if(quoteEntries.isEmpty()){
                        setContentView(R.layout.empty_view);
                }
            }
        });
    }
}
