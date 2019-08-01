var exec = require('cordova/exec');

module.exports.chooseLauncher = function (success, error) {
    exec(success, error, 'Kiosk', 'coolMethod');
};


