package com.example.android.bookcompanion.BookQuoteFeature;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.bookcompanion.R;

import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder> {
    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds task data and the Context
    private List<QuoteEntry> mQuoteEntries;
    private Context mContext;

    public QuoteAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @Override
    public QuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.quote_item, parent, false);

        return new QuoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuoteViewHolder holder, int position) {
        // Determine the values of the wanted data
        QuoteEntry quoteEntry = mQuoteEntries.get(position);
        String quoteContent = quoteEntry.getQuoteContent();
        String quoteBookTitle = quoteEntry.getBookTitle();
        //Set values
        holder.quoteDescription.setText(quoteContent);
        holder.bookTitleQuote.setText(quoteBookTitle);
    }

    @Override
    public int getItemCount() {
        if (mQuoteEntries == null) {
            return 0;
        }
        return mQuoteEntries.size();
    }

    public List<QuoteEntry> getQuotes() {
        return mQuoteEntries;
    }

    public void setQuotes(List<QuoteEntry> quoteEntries) {
        mQuoteEntries = quoteEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class QuoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the task description and priority TextViews
        TextView bookTitleQuote;
        TextView quoteDescription;


        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public QuoteViewHolder(View itemView) {
            super(itemView);

            bookTitleQuote = itemView.findViewById(R.id.QuoteBookTitle);
            quoteDescription = itemView.findViewById(R.id.quoteContent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mQuoteEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }

}
