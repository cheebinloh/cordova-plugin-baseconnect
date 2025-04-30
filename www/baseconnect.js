var exec = require('cordova/exec');

exports.scan = function (success, error) {
    exec(success, error, 'BaseConnect', 'scan', []);
};

// Add more methods like connect, openLock, etc.