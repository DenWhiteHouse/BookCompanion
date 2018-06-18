package com.example.android.bookcompanion;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bookcompanion.room.ReadingTrack;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReadingTrackAdapter extends RecyclerView.Adapter<ReadingTrackAdapter.ReadingTrackViewHolder> {

    private final List<ReadingTrack> list;
    // Constant for date format
    private static final String DATE_FORMAT = "dd/mm/yyyy";
    // Date formatter
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public ReadingTrackAdapter(List<ReadingTrack> list) {
        this.list = list;
    }

    @Override
    public ReadingTrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.readin_track_item, parent, false);
        return new ReadingTrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReadingTrackViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ReadingTrackViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mLocation;
        private TextView mDate;
        private TextView mPages;

        public ReadingTrackViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.TitleReadingtrackTV);
            mLocation = itemView.findViewById(R.id.LocationReadingtrackTV);
            mDate = itemView.findViewById(R.id.DateReadingTrackTV);
            mPages = itemView.findViewById(R.id.PagesReadingTrackTV);
        }

        public void bind(ReadingTrack readingTrack) {
            mTitle.setText(readingTrack.getBookTitle());
            mLocation.setText(readingTrack.getLocation());
            mDate.setText(dateFormat.format(readingTrack.getDate()));
            mPages.setText(readingTrack.getPagesRead());
        }
    }
}
