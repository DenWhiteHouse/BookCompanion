package com.example.android.bookcompanion.widget;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class WidgetService extends IntentService {
    public static String WIDGETSERVICE = "WIDGETSERVICE";
    ;

    public WidgetService() {
        super("WidgetService");
    }

    //getting Quote from AddQuoteActivity
    public static void widgetIntent(Context context, String quote) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(WIDGETSERVICE, quote);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            //Handle the IntentFromTheActivity
            String quotefromActivity = intent.getExtras().getString(WIDGETSERVICE);
            updateWidgetIntent(quotefromActivity);
        }
    }

    private void updateWidgetIntent(String quoteFromActivity) {
        //MaketheIntent for the Provider
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra(WIDGETSERVICE, quoteFromActivity);
        sendBroadcast(intent);
    }


}


