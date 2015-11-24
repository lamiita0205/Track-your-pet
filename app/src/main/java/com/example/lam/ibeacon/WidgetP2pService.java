package com.example.lam.ibeacon;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;

public class WidgetP2pService extends Service implements WifiP2pManager.ActionListener, WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener{

    private WifiP2pManager mManager;
    //The thing returned with p2p
    private WifiP2pManager.Channel mChannel;
    //For them intents inside Android
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;

    private Button connectButton;

    private Collection peerList;

    private boolean threadRun = true;

    private TextView updatebar;

    public WidgetP2pService() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadRun = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Intent intent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

        this.intent=intent;
        Toast.makeText(WidgetP2pService.this, "lala", Toast.LENGTH_SHORT).show();

        Log.i("tag", "msgtiyouythdert7iy8hjg");

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(getApplicationContext(), getMainLooper(), null);
        mReceiver = new MyBroadcastReceiver(mManager, mChannel, WidgetP2pService.this);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver(mReceiver, mIntentFilter);
        mManager.discoverPeers(mChannel, WidgetP2pService.this);
        createP2PGroup();
        return START_NOT_STICKY;
    }

    //PeerListListener
    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        peerList = peers.getDeviceList();
    }

    //ConnectionInfoListener
    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        Log.i("CONNECTION", info.toString());
        //The actual connection is made here
        if(info.groupFormed) {
            if(info.isGroupOwner) {
                Thread t = new Thread(new ServerThread());
                t.start();
            }
        }
    }

    private class ServerThread implements Runnable {

        @Override
        public void run() {
            try {
                ServerSocket server = new ServerSocket(3000);
                while(threadRun) {
                    Socket client = server.accept();
                    DataInputStream input = new DataInputStream(client.getInputStream());
                    int value = input.readInt();
                    client.close();
                    if (value == 0) {
                        //Update the widget to warn the user
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WidgetP2pService.this);
                        int[] allWidgetIds = intent
                                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

                        RemoteViews remoteViews = new RemoteViews(WidgetP2pService.this.getPackageName(),
                                R.layout.connect_widget);

                        Intent statusIntent = new Intent(WidgetP2pService.this,
                                connect_widget.class);

                        statusIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                        statusIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                                allWidgetIds);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, statusIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        remoteViews.setImageViewIcon(R.id.update, Icon.createWithResource(getPackageName(), R.drawable.red));

                        for (int widgetId : allWidgetIds) {

                            appWidgetManager.updateAppWidget(widgetId, remoteViews);

                        }

                    }else {
                        //Update the widget to warn the user
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WidgetP2pService.this);
                        int[] allWidgetIds = intent
                                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

                        RemoteViews remoteViews = new RemoteViews(WidgetP2pService.this.getPackageName(),
                                R.layout.connect_widget);

                        Intent statusIntent = new Intent(WidgetP2pService.this,
                                connect_widget.class);

                        statusIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                        statusIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                                allWidgetIds);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, statusIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        remoteViews.setImageViewIcon(R.id.update, Icon.createWithResource(getPackageName(), R.drawable.green));

                        for (int widgetId : allWidgetIds) {

                            appWidgetManager.updateAppWidget(widgetId, remoteViews);

                        }
                    }
                }
            }catch(Exception e) {

            }
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(int reason) {

    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        private WifiP2pManager mManager;
        private WifiP2pManager.Channel mChannel;
        private WidgetP2pService mActivity;

        public MyBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, WidgetP2pService mActivity) {
            super();
            this.mActivity = mActivity;
            this.mChannel = mChannel;
            this.mManager = mManager;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION))
            {
                //Merge dracia + notify activity
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
                {
                    //merge, all good
                }
                else
                {
                    //Yo, turn wifi on
                }
            }else if(action.equals(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION))
            {
                if(mManager!=null)
                {
                    mManager.requestPeers(mChannel, (WifiP2pManager.PeerListListener) mActivity);
                }
            }else if(action.equals(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION))
            {

            }else if(action.equals(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION))
            {

            }
        }
    }

    private void createP2PGroup() {
        if(peerList == null) {
            return;
        }
        Iterator it = peerList.iterator();
        while(it.hasNext()) {
            WifiP2pDevice device = (WifiP2pDevice) it.next();
            //if(device.deviceAddress.compareToIgnoreCase("5C:51:88:85:69:D0")==0)
            if(device.deviceName.compareToIgnoreCase("lem")==0)   //"24:69:a5:e6:76:e5")==0)  //MAC de la coleg
            {
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                config.groupOwnerIntent = 15;

                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        mManager.requestConnectionInfo(mChannel, WidgetP2pService.this);
                    }

                    @Override
                    public void onFailure(int reason) {
                        //screw this
                    }
                });
                break;
            }
        }
    }
}