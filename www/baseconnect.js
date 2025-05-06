console.log("[BaseConnect] JS file loaded ✅");

var exec = require('cordova/exec');

var BaseConnect = {
    scan: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, "BaseConnectPlugin", "scan", []);
    },
    openLock: function(macAddress, successCallback, errorCallback) {
        exec(successCallback, errorCallback, "BaseConnectPlugin", "openLock", [macAddress]);
    }
};

module.exports = BaseConnect;

if (typeof cordova !== 'undefined' && cordova.plugins) {
    cordova.plugins.BaseConnect = BaseConnect;
}
