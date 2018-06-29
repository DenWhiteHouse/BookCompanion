package com.example.android.bookcompanion;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bookcompanion.room.ReadingTrack;
import com.example.android.bookcompanion.room.ReadingTrackDatabase;
import com.example.android.bookcompanion.room.ReadingTrackViewModel;

import java.util.List;

import butterknife.BindView;

public class MyReadingTracks extends AppCompatActivity {

    ReadingTrackDatabase readingTrackDatabase;
    ReadingTrack mReadingTrack;

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
        mAdapter = new ReadingTrackAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mDb = ReadingTrackDatabase.getInstance(getApplicationContext());
        retrieveTasks();
        setupViewModel();

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyReadingTracks.this);
                builder.setMessage(R.string.swipeAlert);
                builder.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        Thread t = new Thread(new Runnable() {
                            public void run() {
                                int position = viewHolder.getAdapterPosition();
                                List<ReadingTrack> readingtracks = mAdapter.getReadingTracks();
                                readingTrackDatabase.getInstance(getApplicationContext()).getReadingtrackDao().delete(readingtracks.get(position));
                            }
                        });
                        t.start();
                        dialog.cancel();
                        Toast.makeText(MyReadingTracks.this, "item Deleted ", Toast.LENGTH_SHORT).show();
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

    }

    private void retrieveTasks() {
        Log.d(TAG, "Actively retrieving the quotes from the DataBase");
        LiveData<List<ReadingTrack>> tracks = mDb.getReadingtrackDao().getAllReadingTrack();
        tracks.observe(this, new Observer<List<ReadingTrack>>() {
            @Override
            public void onChanged(@Nullable List<ReadingTrack> readingTracks) {
                Log.d(TAG, "Receiving database update from LiveData");
                mAdapter.setTasks(readingTracks);
            }
        });
    }

    private void setupViewModel() {
        ReadingTrackViewModel viewModel = ViewModelProviders.of(this).get(ReadingTrackViewModel.class);
        viewModel.getReadingTrackList().observe(this, new Observer<List<ReadingTrack>>() {
            @Override
            public void onChanged(@Nullable List<ReadingTrack> readingTracks) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mAdapter.setTasks(readingTracks);
                if (readingTracks.isEmpty()) {
                    setContentView(R.layout.empty_view);
                }
            }
        });
    }
}