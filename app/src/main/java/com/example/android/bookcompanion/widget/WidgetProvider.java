package com.example.android.bookcompanion.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Spinner;

import com.example.android.bookcompanion.BookQuoteFeature.AddQuoteActivity;
import com.example.android.bookcompanion.BookQuoteFeature.QuoteDatabase;
import com.example.android.bookcompanion.BookQuoteFeature.QuoteEntry;
import com.example.android.bookcompanion.MainActivity;
import com.example.android.bookcompanion.R;
import com.example.android.bookcompanion.room.ReadingTrack;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;


import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetProvider extends AppWidgetProvider {
    private static QuoteDatabase mDb;
    private static String mLastQuoteTitle,mLastQuoteContent;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            /* //todo DEFINE A SERVICE FOR ACCESSING THE DB
            mDb = QuoteDatabase.getInstance(context);
            getQuoteInfo();
            */
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_view);
            remoteView.setTextViewText(R.id.widget_body_TV, "prova");
            appWidgetManager.updateAppWidget(appWidgetId, remoteView);
        }
    }

    private static void getQuoteInfo(){
        LiveData<List<QuoteEntry>> quotes = mDb.QuoteDao().loadAllQuotes();
        List<QuoteEntry> quoteList =quotes.getValue();
        mLastQuoteContent = quoteList.get(quoteList.size()-1).getQuoteContent();
        mLastQuoteTitle = quoteList.get(quoteList.size()-1).getBookTitle();
    }

}
