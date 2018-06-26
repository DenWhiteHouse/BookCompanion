package com.example.android.bookcompanion;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookcompanion.BookQuoteFeature.MyQuotesViewModel;
import com.example.android.bookcompanion.BookQuoteFeature.QuoteAdapter;
import com.example.android.bookcompanion.BookQuoteFeature.QuoteDatabase;
import com.example.android.bookcompanion.BookQuoteFeature.QuoteEntry;
import com.example.android.bookcompanion.database.BookContract;
import com.example.android.bookcompanion.room.ReadingTrack;
import com.example.android.bookcompanion.room.ReadingTrackDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyLibraryBookDetails extends AppCompatActivity{
    @BindView(R.id.bookTitleLibrary) TextView mBookTitleTV;
    @BindView(R.id.bookAuthorLibrary) TextView mBookAuthorTV;
    @BindView(R.id.bookPagesLibrary) TextView mBookPageTV;
    @BindView(R.id.bookImageLibrary) ImageView mBookImage;
    @BindView(R.id.deleteBookButton) Button mDeleteButtonBook;
    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerViewReadingTracks;
    private RecyclerView mRecyclerViewQuotes;
    private ReadingTrackAdapter mAdapterReadingTracks;
    private QuoteAdapter mAdapterQuotes;
    private ReadingTrackDatabase mDbReadingTracks;
    private QuoteDatabase mDbQuotes;
    private Uri mCurrentBookUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_library_details_activity);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();
        setBookInfo(intent);

        // Set the RecyclerView for ReadingTracks
        mRecyclerViewReadingTracks = findViewById(R.id.reading_track_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewReadingTracks.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(mRecyclerViewReadingTracks.getContext(), layoutManager.getOrientation());
        mRecyclerViewReadingTracks.addItemDecoration(decoration);
        //Set the Adapter
        mAdapterReadingTracks = new ReadingTrackAdapter(this);
        mRecyclerViewReadingTracks.setAdapter(mAdapterReadingTracks);

        mDbReadingTracks = ReadingTrackDatabase.getInstance(getApplicationContext());
        retrieveReadingTracks(intent.getStringExtra("BOOKTITLE"));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyLibraryBookDetails.this);
                builder.setMessage(R.string.swipeAlert);
                builder.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        Thread t = new Thread(new Runnable() {
                            public void run() {
                                int position = viewHolder.getAdapterPosition();
                                List<ReadingTrack> readingtracks = mAdapterReadingTracks.getReadingTracks();
                                mDbReadingTracks.getInstance(getApplicationContext()).getReadingtrackDao().delete(readingtracks.get(position));
                            }
                        });
                        t.start();
                        dialog.cancel();
                        Toast.makeText(MyLibraryBookDetails.this, "item Deleted ", Toast.LENGTH_SHORT).show();
                    };
                });
                builder.setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        mAdapterReadingTracks.notifyItemChanged(viewHolder.getAdapterPosition());
                        dialog.cancel();
                    };
                });
                builder.show();
            }
        }).attachToRecyclerView(mRecyclerViewReadingTracks);

        //Setting RecylerView for Quotes


        mRecyclerViewQuotes = findViewById(R.id.quotes_recycler_view);
        LinearLayoutManager layoutManagerQuotes = new LinearLayoutManager(this);
        mRecyclerViewQuotes.setLayoutManager(layoutManagerQuotes);
        DividerItemDecoration decorationQuotes = new DividerItemDecoration(mRecyclerViewQuotes.getContext(), layoutManager.getOrientation());
        mRecyclerViewQuotes.addItemDecoration(decorationQuotes);

        // Initialize the adapter and attach it to the RecyclerView
        mAdapterQuotes = new QuoteAdapter(this);
        mRecyclerViewQuotes.setAdapter(mAdapterQuotes);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyLibraryBookDetails.this);
                builder.setMessage(R.string.swipeAlert);
                builder.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                int position = viewHolder.getAdapterPosition();
                                List<QuoteEntry> quotes = mAdapterQuotes.getQuotes();
                                mDbQuotes.QuoteDao().deleteQuote(quotes.get(position));
                            }
                        });
                        dialog.cancel();
                        Toast.makeText(MyLibraryBookDetails.this, "item Deleted ", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        mAdapterQuotes.notifyItemChanged(viewHolder.getAdapterPosition());
                        dialog.cancel();
                    };
                });
                builder.show();
            }
        }).attachToRecyclerView(mRecyclerViewQuotes);

        mDbQuotes = QuoteDatabase.getInstance(getApplicationContext());
        setupViewModel(intent.getStringExtra("BOOKTITLE"));

        //Set DELETE BUTTON CLICK LISTENER
        mDeleteButtonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBook();
            }
        });

    }

    private void retrieveReadingTracks(String bookTitle) {
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        LiveData<List<ReadingTrack>> tasks = mDbReadingTracks.getReadingtrackDao().getByTitle(bookTitle);
        tasks.observe(this, new Observer<List<ReadingTrack>>() {
            @Override
            public void onChanged(@Nullable List<ReadingTrack> readingTracks) {
                Log.d(TAG, "Receiving database update from LiveData");
                mAdapterReadingTracks.setTasks(readingTracks);
            }
        });
    }


    private void setBookInfo(Intent intent){
        mBookTitleTV.setText(intent.getStringExtra("BOOKTITLE"));
        mBookAuthorTV.setText(intent.getStringExtra("AUTHOR"));
        mBookPageTV.setText(intent.getStringExtra("PAGES"));
        Uri builtUri = Uri.parse(intent.getStringExtra("IMAGEURL")).buildUpon().build();
        Picasso.with(this).load(builtUri).into(mBookImage);

    }

    private void setupViewModel(String bookTitle) {
        MyQuotesViewModel viewModel = ViewModelProviders.of(this).get(MyQuotesViewModel.class);
        viewModel.getQuotesByTitle(bookTitle).observe(this, new Observer<List<QuoteEntry>>() {
            @Override
            public void onChanged(@Nullable List<QuoteEntry> quoteEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mAdapterQuotes.setQuotes(quoteEntries);
            }
        });
    }

    private void deleteBook(){
        DialogInterface.OnClickListener deleteBookButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int rowsDeleted = getContentResolver().delete(mCurrentBookUri,null , null);
                            Toast.makeText(getApplicationContext(), " così cancella", Toast.LENGTH_SHORT).show();
                            finish();
                    }
                };

        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.warning_delete_book);
        builder.setPositiveButton(R.string.yesDelete, deleteBookButtonClickListener);
        builder.setNegativeButton(R.string.dontDelete, new DialogInterface.OnClickListener() {
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
}
