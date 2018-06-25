package com.example.android.bookcompanion.BookQuoteFeature;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class AddQuoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final QuoteDatabase mDb;
    private final int mID;

    public AddQuoteViewModelFactory(QuoteDatabase database, int ID) {
        mDb = database;
        mID = ID;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddQuoteViewModel(mDb, mID);
    }
}
