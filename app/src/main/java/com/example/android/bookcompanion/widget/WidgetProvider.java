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
    public static String WIDGETSERVICE = "WIDGETSERVICE";
    static String mQuoteContent;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Use the XML file to build the remoteView for the Widger
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_view);
        remoteViews.setTextViewText(R.id.widget_body_TV, mQuoteContent);
        // UPDATE THE WIDGET
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static void updateWidgtes(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
        final String action = intent.getAction();

        if (action.equals("android.appwidget.action.APPWIDGET_UPDATE")) {
            mQuoteContent = intent.getExtras().getString(WIDGETSERVICE);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_body_TV);
            //In case of multiple widgtes
            WidgetProvider.updateWidgtes(context, appWidgetManager, appWidgetIds);
            super.onReceive(context, intent);
        }
    }



}
