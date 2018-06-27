package com.example.android.bookcompanion.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.android.bookcompanion.BookQuoteFeature.AddQuoteActivity;


public class WidgetIntent extends IntentService  {

    public static String QUOTE = "QUOTE";
    public String mQuoteBody;
    public WidgetIntent() {
        super("WidgetIntent");
    }

    public static void widgetIntent(Context context, String bookTitle) {
        Intent intent = new Intent(context, AddQuoteActivity.class);
        intent.putExtra(QUOTE, bookTitle);
        context.startActivity(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mQuoteBody = intent.getStringExtra(QUOTE);
            updateWidgetIntent(mQuoteBody);
        }
    }

    private void updateWidgetIntent(String bookTiles) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(QUOTE, bookTiles);
        sendBroadcast(intent);
    }

}
