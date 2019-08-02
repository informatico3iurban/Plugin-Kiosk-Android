var exec = require('cordova/exec');

module.exports.chooseLauncher = function (success, error) {
    exec(success, error, 'Kiosk', 'chooseLauncher');
};

module.exports.enableImmersiveMode = function (success, error) {
    exec(success, error, 'Kiosk', 'enableImmersiveMode');
};

