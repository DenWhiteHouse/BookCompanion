package com.example.android.bookcompanion;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookcompanion.database.BookContract;
import com.squareup.picasso.Picasso;

public class BookCursorAdapter extends CursorAdapter {

    private Context mContext;

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final long id;
        id =cursor.getLong(cursor.getColumnIndex(BookContract.BookEntry._ID));
        ImageView bookImage = view.findViewById(R.id.bookImageLibrary);
        final TextView booktitle = view.findViewById(R.id.bookTitleLibrary);
        TextView bookauthor = view.findViewById(R.id.bookAuthorLibrary);
        TextView bookpages = view.findViewById(R.id.bookPagesLibrary);

        Uri builtUri = Uri.parse(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COL_BOOK_IMAGE))).buildUpon().build();
        Picasso.with(context).load(builtUri).into(bookImage);
        booktitle.setText(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COL_BOOK_NAME)));
        bookauthor.setText(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COL_BOOK_AUTH)));
        bookpages.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(BookContract.BookEntry.COL_BOOK_PAGES))));

    }

}
