const FingerPrint = require('react-native').NativeModules.FingerPrint;
const DeviceEventEmitter = require('react-native').DeviceEventEmitter;
let fingerprintAuthListener = null;

module.exports = {
  initializeFingerPrintAuth() {
    FingerPrint.initializeFingerPrintAuth();
  },
  registerFingerPrintCallback(callback) {
    fingerprintAuthListener = DeviceEventEmitter.addListener('fingerprintAuth',
      (result) => {
        callback(result);
      });
  },
  removeFingerPrintCallback() {
    if (fingerprintAuthListener) {
      fingerprintAuthListener.remove();
      fingerprintAuthListener = null;
    }
  },
}
