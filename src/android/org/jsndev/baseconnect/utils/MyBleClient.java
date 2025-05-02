package org.jsndev.baseconnect.utils;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
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
            public void onDeviceConnected(@NonNull BluetoothDevice device) {
                Log.d(TAG, "Device connected: " + device.getAddress());
            }

            @Override
            public void onDeviceReady(@NonNull BluetoothDevice device) {
                Log.d(TAG, "Device ready: " + device.getAddress());
            }

            @Override
            public void onDeviceDisconnected(@NonNull BluetoothDevice device) {
                Log.d(TAG, "Device disconnected: " + device.getAddress());
            }

            @Override
            public void onLinkLossOccurred(@NonNull BluetoothDevice device) {
                Log.w(TAG, "Link lost: " + device.getAddress());
            }

            @Override
            public void onDeviceNotSupported(@NonNull BluetoothDevice device) {
                Log.e(TAG, "Device not supported: " + device.getAddress());
            }

            @Override
            public void onError(@NonNull BluetoothDevice device, @NonNull String message, int errorCode) {
                Log.e(TAG, "Error: " + message + " (code: " + errorCode + ")");
            }

            @Override
            public void onEventReport(String uuid, int code, String message) {
                Log.d(TAG, "Event report - UUID: " + uuid + ", Code: " + code + ", Message: " + message);
            }
        });
    }
}
