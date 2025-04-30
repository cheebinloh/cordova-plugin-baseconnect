package org.jsndev.baseconnect.utils;

import android.content.Context;
import android.util.Log;

import com.example.hxjblinklibrary.blinkble.profile.client.HxjBleClient;
import com.example.hxjblinklibrary.blinkble.profile.client.LinkCallBack;

public class MyBleClient extends HxjBleClient {

    private static MyBleClient instance;

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
        });
    }
}
