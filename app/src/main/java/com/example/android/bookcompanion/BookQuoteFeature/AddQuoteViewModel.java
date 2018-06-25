package com.example.android.bookcompanion.BookQuoteFeature;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class AddQuoteViewModel extends ViewModel {
    private LiveData<QuoteEntry> quote;

    public AddQuoteViewModel(QuoteDatabase database, String bookTitle) {
        quote = database.QuoteDao().loadSingleQuoteByTitle(bookTitle);
    }

    public LiveData<QuoteEntry> getQuote() {
        return quote;
    }
}
