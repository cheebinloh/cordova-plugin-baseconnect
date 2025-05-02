package org.jsndev.baseconnect;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import com.example.hxjblinklibrary.blinkble.scanner.HxjBluetoothDevice;
import com.example.hxjblinklibrary.blinkble.entity.Response;
import com.example.hxjblinklibrary.blinkble.entity.requestaction.OpenLockAction;
import com.example.hxjblinklibrary.blinkble.entity.reslut.HxBLEUnlockResult;
import com.example.hxjblinklibrary.blinkble.profile.client.FunCallback;
import com.example.hxjblinklibrary.blinkble.scanner.HxjScanner;
import com.example.hxjblinklibrary.blinkble.scanner.HxjScanCallback;

import com.example.hxjblinklibrary.blinkble.entity.requestaction.BlinkyAuthAction;
import com.example.hxjblinklibrary.blinkble.core.MyBleClient;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import org.jsndev.baseconnect.utils.MyBleClient;

public class BaseConnectPlugin extends CordovaPlugin {
    private static final String TAG = "BaseConnectPlugin";
    private CallbackContext scanCallback;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("scan")) {
            this.scanCallback = callbackContext;
            startScan();
            return true;
        } else if (action.equals("openLock")) {
            String macAddress = args.getString(0);
            openLock(macAddress, callbackContext);
            return true;
        }
        return false;
    }

    private void startScan() {
        Context context = this.cordova.getContext();
        long scanTimeout = 10000; // 10 seconds
        int scanType = 1; // check SDK doc for scan mode: 0 = all, 1 = BLE, etc.

        HxjScanner.getInstance().startScan(scanTimeout, context, new HxjScanCallback() {
            public void onScanStart() {
                Log.d(TAG, "Scan started");
            }

            public void onScanResult(HxjBluetoothDevice device, int rssi, byte[] scanRecord) {
                try {
                    JSONObject deviceJson = new JSONObject();
                    deviceJson.put("name", device.getName());
                    deviceJson.put("mac", device.getMac());
                    deviceJson.put("rssi", rssi);
                    PluginResult result = new PluginResult(PluginResult.Status.OK, deviceJson);
                    result.setKeepCallback(true);
                    scanCallback.sendPluginResult(result);
                } catch (JSONException e) {
                    Log.e(TAG, "Scan JSON error", e);
                }
            }

            public void onScanComplete(List<HxjBluetoothDevice> devices) {
                Log.d(TAG, "Scan complete");
            }
        }, scanType);
    }

    private void openLock(String macAddress, CallbackContext callbackContext) {
        Context context = this.cordova.getContext();
        
        BlinkyAuthAction authAction = new BlinkyAuthAction.Builder()
            .mac(macAddress.toLowerCase()) // SDK expects lowercase MAC
            .keyGroupId(900)
            .bleProtocolVer(12)        // Replace if needed
            .build();

        OpenLockAction actionObj = new OpenLockAction();
        actionObj.setBaseAuthAction(authAction);

        MyBleClient.getInstance(context).openLock(actionObj, new FunCallback<HxBLEUnlockResult>() {
            @Override
            public void onResponse(Response<HxBLEUnlockResult> response) {
                try {
                    JSONObject result = new JSONObject();
                    result.put("code", response.getCode());
                    result.put("message", response.getMessage());
                    result.put("data", response.getBody() != null ? response.getBody().toString() : JSONObject.NULL);
                    callbackContext.success(result);
                } catch (JSONException e) {
                    callbackContext.error("Failed to parse open lock result");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                callbackContext.error("Open lock failed: " + throwable.getMessage());
            }
        });
    }
}
