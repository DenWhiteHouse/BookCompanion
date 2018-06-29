package com.example.android.bookcompanion.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bookcompanion.R;

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
