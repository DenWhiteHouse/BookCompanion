package com.example.android.bookcompanion.BookQuoteFeature;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookcompanion.MyQuotes;
import com.example.android.bookcompanion.R;

import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder> {
    public static final String EXTRA_QUOTE_ID = "extraQuoteId";
    // Class variables for the List that holds task data and the Context
    private List<QuoteEntry> mQuoteEntries;
    private Context mContext;

    public QuoteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public QuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.quote_item, parent, false);

        return new QuoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuoteViewHolder holder, int position) {
        // Determine the values of the wanted data
        final QuoteEntry quoteEntry = mQuoteEntries.get(position);
        String quoteContent = quoteEntry.getQuoteContent();
        final String quoteBookTitle = quoteEntry.getBookTitle();
        //Set values
        holder.quoteDescription.setText(quoteContent);
        holder.bookTitleQuote.setText(quoteBookTitle);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,AddQuoteActivity.class);
                intent.putExtra(EXTRA_QUOTE_ID,quoteEntry.getId());
                mContext.startActivity(intent);
            }
        });
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
    class QuoteViewHolder extends RecyclerView.ViewHolder{

        // Class variables for the task description and priority TextViews
        TextView bookTitleQuote;
        TextView quoteDescription;
        Button editButton;


        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public QuoteViewHolder(View itemView) {
            super(itemView);

            bookTitleQuote = itemView.findViewById(R.id.QuoteBookTitle);
            quoteDescription = itemView.findViewById(R.id.quoteContent);
            editButton =itemView.findViewById(R.id.editQuoteButton);
        }

    }

}
