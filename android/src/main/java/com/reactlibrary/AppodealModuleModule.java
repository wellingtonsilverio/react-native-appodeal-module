package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class AppodealModuleModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public AppodealModuleModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "AppodealModule";
    }

    @ReactMethod
    public void initialize(String key, Callback callback) {
        callback.invoke("Received key: " + key);
    }

    @ReactMethod
    public void showRewarded(Callback callback) {
        callback.invoke("show Rewarded");
    }
}
