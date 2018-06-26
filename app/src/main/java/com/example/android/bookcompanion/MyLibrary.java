package com.example.android.bookcompanion;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

        //Set item Click for Cursor Items
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = mCursorAdapter.getCursor();
                cursor.moveToPosition(i);
                String title = cursor.getString(cursor.getColumnIndex(BookEntry.COL_BOOK_NAME));
                String author = cursor.getString(cursor.getColumnIndex(BookEntry.COL_BOOK_AUTH));
                String imageUrl = cursor.getString(cursor.getColumnIndex(BookEntry.COL_BOOK_IMAGE));
                String pages = cursor.getString(cursor.getColumnIndex(BookEntry.COL_BOOK_PAGES));
                int ID= cursor.getInt(cursor.getColumnIndex(BookEntry._ID));
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI,ID);
                Intent intent = new Intent(MyLibrary.this,MyLibraryBookDetails.class);
                intent.setData(currentBookUri);
                intent.putExtra("BOOKTITLE",title);
                intent.putExtra("IMAGEURL",imageUrl);
                intent.putExtra("AUTHOR",author);
                intent.putExtra("PAGES",pages);
                startActivity(intent);
            }
        });
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
