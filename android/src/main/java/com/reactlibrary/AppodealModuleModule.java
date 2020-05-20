package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.Native;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeAdView;
import com.appodeal.ads.UserSettings;
import com.appodeal.ads.utils.Log;

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
    public void initialize(String APP_KEY) {
        // Add user settings
        Appodeal.setUserAge(25);
        Appodeal.setUserGender(UserSettings.Gender.MALE);
        Appodeal.initialize(this, APP_KEY, Appodeal.NONE, consent);
    }

    @ReactMethod
    public void initializeRewarded(String APP_KEY) {
        Appodeal.initialize(this, APP_KEY, Appodeal.REWARDED_VIDEO, consent);
        Appodeal.setRewardedVideoCallbacks(new AppodealRewardedVideoCallbacks(this));
    }

    @ReactMethod
    public void isRewardedVideoLoaded(String APP_KEY) {
        if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }
    }

    @ReactMethod
    public void rewardedVideoShow(Callback callback) {
        boolean isShown = Appodeal.show(this, Appodeal.REWARDED_VIDEO);

        callback.invoke(isShown);
    }
}
