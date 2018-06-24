package com.example.android.bookcompanion;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookcompanion.room.ReadingTrack;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReadingTrackAdapter extends RecyclerView.Adapter<ReadingTrackAdapter.ReadingTrackViewHolder> {

    private Context mContext;

    private  List<ReadingTrack> mRTracksList;
    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    // Date formatter
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public ReadingTrackAdapter(Context context){
        mContext = context;
    }

    @Override
    public ReadingTrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reading_track_item, parent, false);
        return new ReadingTrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReadingTrackViewHolder holder, int position) {
        final ReadingTrack readingTrack = mRTracksList.get(position);
        final Integer listposition= position;
        holder.mTitle.setText(readingTrack.getBookTitle());
        holder.mLocation.setText(readingTrack.getLocation());
        holder.mDate.setText(String.valueOf(readingTrack.getDate()));
        holder.mPages.setText(String.valueOf(readingTrack.getPagesRead()));
        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"prova " + holder.mTitle.getText().toString(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,EditReadingTrack.class);
                intent.putExtra("ID",readingTrack.getUid());
                intent.putExtra("BOOKTITLE",readingTrack.getBookTitle());
                intent.putExtra("BOOKLOCATION",readingTrack.getLocation());
                intent.putExtra("BOOKDATE",readingTrack.getDate());
                intent.putExtra("BOOKPAGES",String.valueOf(readingTrack.getPagesRead()));
                mContext.startActivity(intent);
            }
        });
    }


    class ReadingTrackViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mLocation;
        private TextView mDate;
        private TextView mPages;
        private Button mEditButton;

        public ReadingTrackViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.TitleReadingtrackTV);
            mLocation = itemView.findViewById(R.id.LocationReadingtrackTV);
            mDate = itemView.findViewById(R.id.DateReadingTrackTV);
            mPages = itemView.findViewById(R.id.PagesReadingTrackTV);
            mEditButton = itemView.findViewById(R.id.editTrackButton);
        }


    }

    @Override
    public int getItemCount() {
        if (mRTracksList == null) {
            return 0;
        }
        return mRTracksList.size();
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setTasks(List<ReadingTrack> readingTracks) {
        mRTracksList = readingTracks;
        notifyDataSetChanged();
    }

    public List<ReadingTrack> getReadingTracks() {
        return mRTracksList;
    }

}
