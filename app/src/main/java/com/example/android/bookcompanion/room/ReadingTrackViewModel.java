package com.example.android.bookcompanion.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ReadingTrackViewModel extends AndroidViewModel {

    private final LiveData<List<ReadingTrack>> readingTrackList;

    private ReadingTrackDatabase readingTrackDatabase;

    public ReadingTrackViewModel(Application application) {
        super(application);

        readingTrackDatabase = ReadingTrackDatabase.getInstance(this.getApplication());
        readingTrackList = readingTrackDatabase.getReadingtrackDao().getAllReadingTrack();
    }


    public LiveData<List<ReadingTrack>> getReadingTrackList() {
        return readingTrackList;
    }

    public void deleteItem(ReadingTrack readingTrack) {
        new deleteAsyncTask(readingTrackDatabase).execute(readingTrack);
    }

    private static class deleteAsyncTask extends AsyncTask<ReadingTrack, Void, Void> {

        private ReadingTrackDatabase db;

        deleteAsyncTask(ReadingTrackDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final ReadingTrack... params) {
            db.getReadingtrackDao().delete(params[0]);
            return null;
        }

    }
}
