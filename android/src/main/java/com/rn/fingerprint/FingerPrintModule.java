package com.rn.fingerprint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.Log;

import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;

public class FingerPrintModule extends ReactContextBaseJavaModule {
    final private Activity mActivity;

    public FingerPrintModule(ReactApplicationContext reactContext, final Activity activity) {
        super(reactContext);
        mActivity = activity;
    }

    @Override
    public String getName() {
        return "FingerPrint";
    }

    @ReactMethod
    public void authenticate(Callback callback) {
        
    }
}
