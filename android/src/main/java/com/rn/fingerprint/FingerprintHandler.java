package com.rn.fingerprint;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

    private CancellationSignal cancellationSignal;
    private ReactApplicationContext appContext;

    public FingerprintHandler(ReactApplicationContext context) {
        appContext = context;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();

        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        //creates unneccessary noise
        Toast.makeText(appContext, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
        callback("error: " + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Toast.makeText(appContext, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(appContext, "Authentication failed", Toast.LENGTH_LONG).show();
        callback("Authentication failed");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        //UserAuthenticated.getInstance().setValue(true);
        Toast.makeText(appContext,"Authentication succeeded",Toast.LENGTH_LONG).show();
        callback("Authentication succeeded");
    }

    private void callback(String value) {
        WritableMap params = Arguments.createMap();
        params.putString("fingerprint", value);

        appContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("fingerprintAuth", params);

    }
}