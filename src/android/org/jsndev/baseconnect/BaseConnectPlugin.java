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
import org.jsndev.baseconnect.utils.MyBleClient;

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
            Log.d(TAG, "Scan method called");
            this.scanCallback = callbackContext;
            startScan();
            return true;
        } else if (action.equals("openLock")) {
            Log.d(TAG, "openlock method called");
            String macAddress = args.getString(0);
            openLock(macAddress, callbackContext);
            return true;
        }
        return false;
    }

    private void startScan() {
        Context context = this.cordova.getContext();
        long scanTimeout = 5000; // 10 seconds
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
            .keyGroupId(900).build();

        OpenLockAction actionObj = new OpenLockAction();
        actionObj.setBaseAuthAction(authAction);

        MyBleClient.getInstance(context).openLock(actionObj, new FunCallback<HxBLEUnlockResult>() {
            @Override
            public void onResponse(Response<HxBLEUnlockResult> response) {
                try {
                    Class<?> responseClass = response.getClass();

                    // Access private fields using reflection
                    java.lang.reflect.Field codeField = responseClass.getDeclaredField("code");
                    java.lang.reflect.Field messageField = responseClass.getDeclaredField("message");
                    java.lang.reflect.Field dataField = responseClass.getDeclaredField("data");

                    codeField.setAccessible(true);
                    messageField.setAccessible(true);
                    dataField.setAccessible(true);

                    int code = (int) codeField.get(response);
                    String message = (String) messageField.get(response);
                    Object data = dataField.get(response);

                    JSONObject result = new JSONObject();
                    result.put("code", code);
                    result.put("message", message);
                    result.put("data", data != null ? data.toString() : JSONObject.NULL);

                    callbackContext.success(result);
                } catch (Exception e) {
                    callbackContext.error("Reflection failed: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                callbackContext.error("Open lock failed: " + throwable.getMessage());
            }
        });
    }

}
