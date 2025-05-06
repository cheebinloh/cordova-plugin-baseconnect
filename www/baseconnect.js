var exec = require('cordova/exec');

var BaseConnect = {
    scan: function(successCallback, errorCallback) {
        console.log("[BaseConnect.js] Plugin loaded");
        exec(successCallback, errorCallback, "BaseConnectPlugin", "scan", []);
    },

    openLock: function(macAddress, successCallback, errorCallback) {
        exec(successCallback, errorCallback, "BaseConnectPlugin", "openLock", [macAddress]);
    }
};

module.exports = BaseConnect;

// Optional: expose as cordova.plugins.BaseConnect
if (typeof cordova !== 'undefined' && cordova.plugins) {
    cordova.plugins.BaseConnect = BaseConnect;
}
