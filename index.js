const FingerPrint = require('react-native').NativeModules.FingerPrint;
const DeviceEventEmitter = require('react-native').DeviceEventEmitter;
let fingerprintAuthListener = null;

module.exports = {
  initializeFingerPrintAuth() {
    FingerPrint.initializeFingerPrintAuth();
  },
  registerFingerPrintCallback() {
    fingerprintAuthListener = DeviceEventEmitter.addListener('fingerprintAuth',
      (result) => {
       console.info('result', result);
      });
  },
  removeFingerPrintCallback() {
    if (fingerprintAuthListener) {
      fingerprintAuthListener.remove();
      fingerprintAuthListener = null;
    }
  }
}
