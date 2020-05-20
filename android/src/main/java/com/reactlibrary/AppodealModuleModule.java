package com.reactlibrary;

import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.ads.utils.Log;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

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
        Appodeal.setTesting(true);
        Appodeal.setLogLevel(Log.LogLevel.verbose);
        // Add user settings
        Appodeal.setUserAge(25);
        Appodeal.setUserGender(UserSettings.Gender.MALE);
        Appodeal.initialize(getCurrentActivity(), APP_KEY, Appodeal.NONE, consent);
    }

    @ReactMethod
    public void initializeRewarded(String APP_KEY) {
        Appodeal.initialize(getCurrentActivity(), APP_KEY, Appodeal.REWARDED_VIDEO, consent);
        Appodeal.setRewardedVideoCallbacks(new AppodealRewardedVideoCallbacks(getCurrentActivity()));
    }

    @ReactMethod
    public void isRewardedVideoLoaded() {
        if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
            Toast.makeText(getCurrentActivity(), "true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getCurrentActivity(), "false", Toast.LENGTH_SHORT).show();
        }
    }

    @ReactMethod
    public void rewardedVideoShow(Callback callback) {
        boolean isShown = Appodeal.show(getCurrentActivity(), Appodeal.REWARDED_VIDEO);

        callback.invoke(isShown);
    }

//    public void rewardedVideoChooseNetworks() {
//        disableNetworks(Appodeal.REWARDED_VIDEO);
//    }

    @ReactMethod
    public void rewardedVideoCache() {
        Appodeal.cache(getCurrentActivity(), Appodeal.REWARDED_VIDEO);
    }

    private class AppodealRewardedVideoCallbacks implements RewardedVideoCallbacks {
        private final Activity activity;

        AppodealRewardedVideoCallbacks(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onRewardedVideoLoaded(boolean isPrecache) {
            showToast(activity, "onRewardedVideoLoaded, isPrecache: " + isPrecache);
        }

        @Override
        public void onRewardedVideoFailedToLoad() {
            showToast(activity, "onRewardedVideoFailedToLoad");
        }

        @Override
        public void onRewardedVideoShown() {
            showToast(activity, "onRewardedVideoShown");
        }

        @Override
        public void onRewardedVideoShowFailed() {
            showToast(activity, "onRewardedVideoShowFailed");
        }

        @Override
        public void onRewardedVideoClicked() {
            showToast(activity, "onRewardedVideoClicked");
        }

        @Override
        public void onRewardedVideoFinished(double amount, String name) {
            showToast(activity,
                    String.format(Locale.ENGLISH, "onRewardedVideoFinished. Reward: %.2f %s", amount, name));
        }

        @Override
        public void onRewardedVideoClosed(boolean finished) {
            showToast(activity, String.format("onRewardedVideoClosed,  finished: %s", finished));
        }

        @Override
        public void onRewardedVideoExpired() {
            showToast(activity, "onRewardedVideoExpired");
        }

        void showToast(Activity activity, String text) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        }
    }
}
