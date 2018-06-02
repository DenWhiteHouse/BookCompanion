package com.example.android.bookcompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.android.bookcompanion.data.Book;
import com.example.android.bookcompanion.data.Item;
import com.example.android.bookcompanion.data.VolumeInfo;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    ArrayList<Book> mBooksList;
    Context mContext;

    public BookAdapter(Context context) {
        super(context,0);
        mContext=context;
    }

    public void setBook(ArrayList books){
        mBooksList=books;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_results_fragment, parent, false);
        }
        Book currentBook = getItem(position);

        //GET FIELDS

        return listItemView;

    }
}

