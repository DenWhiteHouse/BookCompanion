package com.example.android.bookcompanion;

import android.content.Context;
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

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    private Context mContext;

    private  List<ReadingTrack> mRTracksList;
    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    // Date formatter
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public ReadingTrackAdapter(Context context, ItemClickListener listener ){
        mContext = context;
        mItemClickListener = listener;
    }

    @Override
    public ReadingTrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.readin_track_item, parent, false);
        return new ReadingTrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReadingTrackViewHolder holder, int position) {
        holder.bind(mRTracksList.get(position));
    }


    class ReadingTrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            mDate.setText(String.valueOf(readingTrack.getDate()));
            mPages.setText(String.valueOf(readingTrack.getPagesRead()));
        }

        @Override
        public void onClick(View view) {
            int elementId = mRTracksList.get(getAdapterPosition()).getUid();
            mItemClickListener.onItemClickListener(elementId);
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

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }
}
