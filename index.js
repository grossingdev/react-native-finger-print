var FingerPrint = require('react-native').NativeModules.FingerPrint;

module.exports = {
  listenFingerPrintAuthentication(callback) {
    FingerPrint.authenticate((error, success) => {

    });
  }
}
