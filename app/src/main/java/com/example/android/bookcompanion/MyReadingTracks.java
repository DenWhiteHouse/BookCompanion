package com.example.android.bookcompanion;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Layout;
import android.util.Log;

import com.example.android.bookcompanion.room.ReadingTrack;
import com.example.android.bookcompanion.room.ReadingTrackDatabase;

import java.util.List;

public class MyReadingTracks extends AppCompatActivity implements ReadingTrackAdapter.ItemClickListener {

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private ReadingTrackAdapter mAdapter;
    private ReadingTrackDatabase mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_reading_tracks);

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.reading_track_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(decoration);
        //Set the Adapter
        mAdapter = new ReadingTrackAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        mDb = ReadingTrackDatabase.getInstance(getApplicationContext());
        retrieveTasks();

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
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
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

    @Override
    public void onItemClickListener(int itemId) {
        /*
        Intent intent = new Intent(MainActivity.this, AddReadingTrack.class);
        intent.putExtra(AddReadingTrack.EXTRA_TASK_ID, itemId);
        startActivity(intent);
        */
    }

    private void retrieveTasks() {
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        LiveData<List<ReadingTrack>> tasks = mDb.getReadingtrackDao().getAllReadingTrack();
        tasks.observe(this, new Observer<List<ReadingTrack>>() {
            @Override
            public void onChanged(@Nullable List<ReadingTrack> readingTracks) {
                Log.d(TAG, "Receiving database update from LiveData");
                mAdapter.setTasks(readingTracks);
            }
        });
    }
}