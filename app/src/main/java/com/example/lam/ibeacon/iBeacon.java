package com.example.lam.ibeacon;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.ArraySet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Set;

import static android.bluetooth.BluetoothAdapter.*;

public class iBeacon extends Activity {

    private static String BEACON_UUID = "f7826da6-4fa2-4e98-8024-bc5b71e0893e";

    //Laboratory 13
    private static int BEACON_MAJOR_1 = 3793;
    private static int BEACON_MINOR_1 = 3569;

    //Gork
    private static int BEACON_MAJOR_2 = 28629;
    private static int BEACON_MINOR_2 = 40160;

    //iBaliza
    private static int BEACON_MAJOR_3 = 19686;
    private static int BEACON_MINOR_3 = 1936;

    private Collection<BluetoothDevice> deviceList = new ArraySet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();
        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 0);
        }

        Runnable find = new FindDevices(btAdapter);
        find.run();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private  LeScanCallback leScanCallback = new LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            String devices = "";
            //Collection<BluetoothDevice> deviceList = new ArraySet<>();
            deviceList.add(device);
            Iterator it = deviceList.iterator();
            while(it.hasNext()) {
                BluetoothDevice dev = (BluetoothDevice)it.next();
                devices+=dev.getName()+"\n";
            }
            TextView text = (TextView)findViewById(R.id.devices);
            text.setText(devices);
        }
    };

    private class FindDevices implements Runnable {

        BluetoothAdapter btAdapter;

        public FindDevices(BluetoothAdapter btAdapter) {
            this.btAdapter = btAdapter;
        }

        @Override
        public void run() {
            btAdapter.startLeScan(leScanCallback);
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            btAdapter.stopLeScan(leScanCallback);

            Iterator it = deviceList.iterator();
            while(it.hasNext()) {
                BluetoothDevice device = (BluetoothDevice)it.next();
                BluetoothGatt bluetoothGatt = device.connectGatt(getApplicationContext(), false, btleGattCallback);
            }
        }
    };

    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // this will get called anytime you perform a read or write characteristic operation
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            if(newState == BluetoothProfile.STATE_CONNECTED) {
                Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            // this will get called after the client initiates a            BluetoothGatt.discoverServices() call
        }
    };
}
