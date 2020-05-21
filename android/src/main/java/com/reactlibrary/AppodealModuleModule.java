package com.reactlibrary;

import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.ads.utils.Log;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
//import com.appodeal.ads.Native;
//import com.appodeal.ads.NativeAd;
//import com.appodeal.ads.NativeAdView;
import com.appodeal.ads.UserSettings;

import java.util.Locale;
//import com.appodeal.ads.utils.Log;

public class AppodealModuleModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private final boolean consent = false;

    SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public AppodealModuleModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "AppodealModule";
    }

    @ReactMethod
    public void setTesting(boolean testing) {
        Appodeal.setTesting(testing);
    }

    public void setUserAge(int age) {
        Appodeal.setUserAge(age);
    }

    public void setUserGender(String gender) {
        if (gender == "female") {
            Appodeal.setUserGender(UserSettings.Gender.FEMALE);
        } else {
            Appodeal.setUserGender(UserSettings.Gender.MALE);
        }
    }

    @ReactMethod
    public void initialize(String APP_KEY) {
        Appodeal.initialize(getCurrentActivity(), APP_KEY, Appodeal.NONE, consent);
    }

    @ReactMethod
    public void initializeRewarded(String APP_KEY) {
        Appodeal.setAutoCache(Appodeal.REWARDED_VIDEO, true);
        Appodeal.initialize(getCurrentActivity(), APP_KEY, Appodeal.REWARDED_VIDEO, consent);
        Appodeal.setRewardedVideoCallbacks(new AppodealRewardedVideoCallbacks(getCurrentActivity()));
    }

    @ReactMethod
    public void isRewardedVideoLoaded(Callback callback) {
        if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
            callback.invoke(true);
        } else {
            callback.invoke(false);
        }
    }

    @ReactMethod
    public void rewardedVideoShow(Callback callback) {
        boolean isShown = Appodeal.show(getCurrentActivity(), Appodeal.REWARDED_VIDEO);

        callback.invoke(isShown);
    }

    @ReactMethod
    public void rewardedVideoCache() {
        Appodeal.cache(getCurrentActivity(), Appodeal.REWARDED_VIDEO);
    }

    public void emmitEvent(final String eventName, String... args) {
        WritableMap map = Arguments.createMap();

        map.putString("datetime", formatterDate.format(new Date()));

        int i = 0;
        for (String arg : args) {
            map.putString(String.valueOf(i), arg);
        }

        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, map);
    }

    private class AppodealRewardedVideoCallbacks implements RewardedVideoCallbacks {
        private final Activity activity;

        AppodealRewardedVideoCallbacks(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onRewardedVideoLoaded(boolean isPrecache) {
            showToast(activity, "onRewardedVideoLoaded, isPrecache: " + isPrecache);
            emmitEvent("onRewardedVideoLoaded");
        }

        @Override
        public void onRewardedVideoFailedToLoad() {
            showToast(activity, "onRewardedVideoFailedToLoad");
            emmitEvent("onRewardedVideoFailedToLoad");
        }

        @Override
        public void onRewardedVideoShown() {
            showToast(activity, "onRewardedVideoShown");
            emmitEvent("onRewardedVideoShown");
        }

        @Override
        public void onRewardedVideoShowFailed() {
            showToast(activity, "onRewardedVideoShowFailed");
            emmitEvent("onRewardedVideoShowFailed");
        }

        @Override
        public void onRewardedVideoClicked() {
            showToast(activity, "onRewardedVideoClicked");
            emmitEvent("onRewardedVideoClicked");
        }

        @Override
        public void onRewardedVideoFinished(double amount, String name) {
            showToast(activity,
                    String.format(Locale.ENGLISH, "onRewardedVideoFinished. Reward: %.2f %s", amount, name));
            emmitEvent("onRewardedVideoFinished", String.valueOf(amount), String.valueOf(name));
        }

        @Override
        public void onRewardedVideoClosed(boolean finished) {
            showToast(activity, String.format("onRewardedVideoClosed,  finished: %s", finished));
            emmitEvent("onRewardedVideoClosed");
        }

        @Override
        public void onRewardedVideoExpired() {
            showToast(activity, "onRewardedVideoExpired");
            emmitEvent("onRewardedVideoExpired");
        }

        void showToast(Activity activity, String text) {
            // Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();

            WritableMap map = Arguments.createMap();

            map.putString("message", text);

            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("log", map);
        }
    }
}
