package com.rn.fingerprint;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FingerPrintModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;

    private static final String KEY_NAME = "example_key";

    public FingerPrintModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "FingerPrint";
    }

    @ReactMethod
    public void initializeFingerPrintAuth() {
        keyguardManager = (KeyguardManager) reactContext.getSystemService(Context.KEYGUARD_SERVICE);

        fingerprintManager = (FingerprintManager) reactContext.getSystemService(Context.FINGERPRINT_SERVICE);


        if(!keyguardManager.isKeyguardSecure())
        {
            Toast.makeText(reactContext, "lock screen not enabled",Toast.LENGTH_SHORT).show();
            return;
        }


        if(ActivityCompat.checkSelfPermission(reactContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(reactContext,"Fingerprint auth is not permitted",Toast.LENGTH_SHORT).show();
            return;
        }


        if(!fingerprintManager.hasEnrolledFingerprints()) {
            Toast.makeText(reactContext, "Please register at least one fingerprint", Toast.LENGTH_LONG).show();
            return;
        }

        generateKey();

        if(cipherInit()){

            cryptoObject = new FingerprintManager.CryptoObject(cipher);
            FingerprintHandler helper = new FingerprintHandler(reactContext);
            helper.startAuth(fingerprintManager,cryptoObject);
        }
    }

    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
        }catch(NoSuchAlgorithmException | NoSuchProviderException e ) {
            throw new RuntimeException("Failed to get KeyGenerator instance",e);
        }


        try{
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();
        } catch(NoSuchAlgorithmException|InvalidAlgorithmParameterException |CertificateException | IOException e){
            throw new RuntimeException(e);
        }

    }

    public boolean cipherInit(){

        try{
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES +"/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException e){
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try{

            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,null);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            return true;
        }catch(KeyStoreException | CertificateException | UnrecoverableKeyException | IOException|NoSuchAlgorithmException| InvalidKeyException e){
            throw new RuntimeException("Failed to init cipher",e);
        }
    }
}
