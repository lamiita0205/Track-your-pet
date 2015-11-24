package com.example.lam.ibeacon;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Lam on 17-Nov-15.
 */
public class connect_widget extends AppWidgetProvider {

    private static boolean status = false;

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.connect_widget);

        remoteViews.setOnClickPendingIntent(R.id.startbtn, buildButtonPendingIntent(context));
        remoteViews.setOnClickPendingIntent(R.id.stopbtn, buildButtonPendingIntent(context));

        pushWidgetUpdate(context, remoteViews);
    }

    public static PendingIntent buildButtonPendingIntent(Context context) {
        if(status == false)
        {
            status = true;
            Intent intent = new Intent();
            intent.setAction("ACTIVATE");
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        else {
            status = false;
            Intent intent = new Intent();
            intent.setAction("DEACTIVATE");
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, connect_widget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

}
