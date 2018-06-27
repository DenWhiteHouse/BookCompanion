package com.example.android.bookcompanion.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Spinner;

import com.example.android.bookcompanion.BookQuoteFeature.AddQuoteActivity;
import com.example.android.bookcompanion.R;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;


import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetProvider extends AppWidgetProvider {
    public static String QUOTE = "QUOTE";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_view);
        remoteView.setTextViewText(R.id.TitleWidgetView,"prova");

        //Set the click listener for the save Button
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.TitleWidgetView, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteView);

    }

    //Iterate for multiple widgets
    public static void updateWidgtes(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
        final String action = intent.getAction();

        if (action.equals("android.appwidget.action.APPWIDGET_UPDATE2")) {
            String quote = intent.getExtras().getString(QUOTE);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.WidgetEditView);
            //In case of multiple widgtes
            WidgetProvider.updateWidgtes(context, appWidgetManager, appWidgetIds);
            super.onReceive(context, intent);
        }
        */
    }

}
