package com.example.lam.ibeacon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by Lam on 18-Nov-15.
 */
public class WidgetIntentReceiver extends BroadcastReceiver {
    private static final String status = "Start";

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.connect_widget);
        String action = intent.getAction();
        if(action.equals("ACTIVATE"))
        {
            updateButtonListenerActivate(context);

        }
        else if(action.equals("DEACTIVATE"))
        {
            updateButtonListenerDeactivate(context);

        }

    }

    private void updateButtonListenerActivate(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.connect_widget);

        Toast.makeText(context, "Start pressed", Toast.LENGTH_SHORT).show();

        //REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
        remoteViews.setOnClickPendingIntent(R.id.startbtn, connect_widget.buildButtonPendingIntent(context));
        connect_widget.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
    }

    private void updateButtonListenerDeactivate(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.connect_widget);

        Toast.makeText(context, "Stop pressed", Toast.LENGTH_SHORT).show();

        //REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
        remoteViews.setOnClickPendingIntent(R.id.startbtn, connect_widget.buildButtonPendingIntent(context));
        connect_widget.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
    }


}
