package com.example.android.bookcompanion;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.bookcompanion.database.BookContract;

public class BookCursorAdapter extends CursorAdapter {

    private MainActivity activity = new MainActivity();

    public BookCursorAdapter(MainActivity context, Cursor c) {
        super(context, c);
        this.activity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final long id;
        id =cursor.getLong(cursor.getColumnIndex(BookContract.BookEntry._ID));
        TextView booktitle = view.findViewById(R.id.bookTitleLibrary);
        TextView bookauthor = view.findViewById(R.id.bookAuthorLibrary);
        TextView bookpages = view.findViewById(R.id.bookPagesLibrary);

        booktitle.setText(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COL_BOOK_NAME)));
        bookauthor.setText(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COL_BOOK_AUTH)));
        bookpages.setText(cursor.getInt(cursor.getColumnIndex(BookContract.BookEntry.COL_BOOK_PAGES)));
    }

}
