var exec = require('cordova/exec');

exports.scan = function(success, error) {
    exec(success, error, "BaseConnectPlugin", "scan", []);
};

exports.openLock = function(mac, success, error) {
    exec(success, error, "BaseConnectPlugin", "openLock", [mac]);
};