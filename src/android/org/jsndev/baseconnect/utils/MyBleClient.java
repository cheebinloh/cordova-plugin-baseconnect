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
            public void onDeviceConnected(@NonNull BluetoothDevice device) {
                Log.d(TAG, "Device connected: " + device.getAddress());
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
            public void onError(@NonNull BluetoothDevice device, @NonNull String message, int errorCode) {
                Log.e(TAG, "Error on device " + device.getAddress() + ": " + message + " (code " + errorCode + ")");
            }

            @Override
            public void onEventReport(String uuid, int code, String message) {
                Log.d(TAG, "Event report - UUID: " + uuid + ", Code: " + code + ", Message: " + message);
            }

            @Override
            public void onLinkLossOccurred(@NonNull BluetoothDevice device) {
                Log.w(TAG, "Link loss: " + device.getAddress());
            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device) {
                Log.d(TAG, "Device disconnected: " + device.getAddress());

            }
        });
    }
}
