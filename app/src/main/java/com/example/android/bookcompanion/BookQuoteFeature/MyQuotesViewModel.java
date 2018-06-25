package com.example.android.bookcompanion.BookQuoteFeature;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

public class MyQuotesViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MyQuotesViewModel.class.getSimpleName();

    private LiveData<List<QuoteEntry>> quotes;

    public MyQuotesViewModel(Application application) {
        super(application);
        QuoteDatabase database = QuoteDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        quotes = database.QuoteDao().loadAllQuotes();
    }


    public LiveData<List<QuoteEntry>> getQuotes() {
        return quotes;
    }

    public LiveData<List<QuoteEntry>> getQuotesByTitle(String bookTitle) {
        QuoteDatabase database = QuoteDatabase.getInstance(this.getApplication());
        return database.QuoteDao().loadQuoteByTitle(bookTitle);
    }
}
