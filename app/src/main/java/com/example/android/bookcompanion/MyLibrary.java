package com.example.android.bookcompanion;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.example.android.bookcompanion.data.Book;
import com.example.android.bookcompanion.database.BookContract;
import com.example.android.bookcompanion.database.BookContract.BookEntry;

import java.util.ArrayList;
import java.util.List;

public class MyLibrary extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 0;
    BookCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylibrary_activity);
        final ListView booksListView = (ListView) findViewById(R.id.list);
        mCursorAdapter = new BookCursorAdapter(getApplicationContext(), null);
        booksListView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COL_BOOK_IMAGE,
                BookEntry.COL_BOOK_NAME, BookEntry.COL_BOOK_AUTH,
                BookEntry.COL_BOOK_PAGES, BookEntry.COL_BOOK_START_DATE, BookEntry.COL_BOOK_START_DATE};

        return new CursorLoader(this,
                BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
