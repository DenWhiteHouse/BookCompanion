package com.example.android.bookcompanion.BookQuoteFeature;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class AddQuoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final QuoteDatabase mDb;
    private final String mBookTitle;

    public AddQuoteViewModelFactory(QuoteDatabase database, String bookTitle) {
        mDb = database;
        mBookTitle = bookTitle;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddQuoteViewModel(mDb, mBookTitle);
    }
}
