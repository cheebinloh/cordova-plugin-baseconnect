package org.jsndev.baseconnect.utils;

import android.content.Context;
import android.util.Log;

import com.example.hxjblinklibrary.blinkble.profile.client.HxjBleClient;
import com.example.hxjblinklibrary.blinkble.profile.client.LinkCallBack;
import android.bluetooth.BluetoothDevice;

public class MyBleClient extends HxjBleClient {

    private static MyBleClient instance;
    private static final String TAG = "MyBleClient";

    private MyBleClient(Context context) {
        super(context);
        initCallbacks();
    }

    public static MyBleClient getInstance(Context context) {
        if (instance == null) {
            instance = new MyBleClient(context);
        }
        return instance;
    }

    private void initCallbacks() {
        setLinkCallBack(new LinkCallBack() {
            @Override
            public void onDeviceReady(BluetoothDevice device) {
                Log.d(TAG, "Device ready: " + device.getAddress());
            }

            @Override
            public void onConnecting(String mac) {
                Log.d("MyBleClient", "Connecting to " + mac);
            }

            @Override
            public void onConnected(String mac) {
                Log.d("MyBleClient", "Connected to " + mac);
            }

            @Override
            public void onDisconnected(String mac) {
                Log.d("MyBleClient", "Disconnected from " + mac);
            }

            @Override
            public void onConnectFailed(String mac, Throwable throwable) {
                Log.e("MyBleClient", "Failed to connect to " + mac, throwable);
            }

            @Override
            public void onServiceDiscoverFail(String mac) {
                Log.e("MyBleClient", "Service discovery failed for " + mac);
            }

            @Override
            public void onServiceDiscoverSucceed(String mac) {
                Log.d("MyBleClient", "Service discovery succeeded for " + mac);
            }

            @Override
            public void onDeviceNotSupported(BluetoothDevice device) {
                Log.e("LinkCallBack", "Device not supported: " + device.getAddress());
            }

            @Override
            public void onError(BluetoothDevice device, String message, int code) {
                Log.e("LinkCallBack", "Error: " + message + " Code: " + code);
            }

            @Override
            public void onEventReport(String mac, int code, String message) {
                // Example: log or handle BLE event
                Log.d("MyBleClient", "onEventReport: mac=" + mac + ", code=" + code + ", message=" + message);
            }

            @Override
            public void onLinkLossOccurred(BluetoothDevice device) {
                // handle disconnection or link loss
            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device) {
                Log.d(TAG, "Device disconnected: " + device.getAddress());

            }
        });
    }
}
