package com.example.lam.ibeacon;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Lam on 17-Nov-15.
 */
public class connect_widget extends AppWidgetProvider {

    private static final String MyOnClick = "Start";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        Log.i("ExampleWidget", "Updating widgets " + Arrays.asList(appWidgetIds));



        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch connect_widget activity
            Intent intent = new Intent(context, listener.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.connect_widget);
            views.setOnClickPendingIntent(R.id.startbtn, pendingIntent);



            // Tell the AppWidgetManager to perform an update on the current app
            // widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

            Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();

        }
    }


}
