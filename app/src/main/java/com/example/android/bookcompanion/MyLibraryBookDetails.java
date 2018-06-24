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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookcompanion.room.ReadingTrack;
import com.example.android.bookcompanion.room.ReadingTrackDatabase;
import com.example.android.bookcompanion.room.ReadingTrackViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyLibraryBookDetails extends AppCompatActivity {
    @BindView(R.id.bookTitleLibrary) TextView mBookTitleTV;
    @BindView(R.id.bookAuthorLibrary) TextView mBookAuthorTV;
    @BindView(R.id.bookPagesLibrary) TextView mBookPageTV;
    @BindView(R.id.bookImageLibrary) ImageView mBookImage;

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private ReadingTrackAdapter mAdapter;
    private ReadingTrackDatabase mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_library_details_activity);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        setBookInfo(intent);

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.reading_track_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(decoration);
        //Set the Adapter
        mAdapter = new ReadingTrackAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mDb = ReadingTrackDatabase.getInstance(getApplicationContext());
        retrieveTasks(intent);

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
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
                                List<ReadingTrack> readingtracks = mAdapter.getReadingTracks();
                                mDb.getInstance(getApplicationContext()).getReadingtrackDao().delete(readingtracks.get(position));
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
                        mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        dialog.cancel();
                    };
                });
                builder.show();
            }
        }).attachToRecyclerView(mRecyclerView);

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddTaskActivity.
         */
        /*FloatingActionButton fabButton = findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(MyLibraryBookDetails.this, AddReadingTrack.class);
                startActivity(addTaskIntent);
            }
        });
        */


    }

    /**
     * This method is called after this activity has been paused or restarted.
     * Often, this is after new data has been inserted through an AddTaskActivity,
     * so this re-queries the database data for any changes.
     */



    private void retrieveTasks(Intent intent) {
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        LiveData<List<ReadingTrack>> tasks = mDb.getReadingtrackDao().getByTitle(intent.getStringExtra("BOOKTITLE"));
        tasks.observe(this, new Observer<List<ReadingTrack>>() {
            @Override
            public void onChanged(@Nullable List<ReadingTrack> readingTracks) {
                Log.d(TAG, "Receiving database update from LiveData");
                mAdapter.setTasks(readingTracks);
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

}
