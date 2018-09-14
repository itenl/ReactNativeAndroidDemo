package com.example.cmacking.reactnativeandroiddemo;

import android.app.Application;
import android.os.Bundle;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {

        /**
         * Returns whether dev mode should be enabled.
         * This enables e.g. the dev menu.
         */
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        /**
         * A list of packages used by the app. If the app uses additional views
         * or modules besides the default ones, add more packages here.
         */
        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new MyReactPackage()
            );
        }

        @Override
        protected String getJSMainModuleName() {
            return "index";
        }

    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
    }

    private static Bundle sBundle;
    private static HashMap hashMap;

    public static void setRnCommonActivityInitProps(int entrance) {
        if (hashMap == null) {
            hashMap = new HashMap();
            hashMap.put(1, "HelloWorld");
            hashMap.put(2, "TestView");
            hashMap.put(3, "GetAllNativeModules");
        }
        sBundle = new Bundle();
        sBundle.putString("COMPONENT_TYPE", hashMap.get(entrance).toString());
    }

    public static void setRnCommonActivityInitProps(String entrance) {
        sBundle = new Bundle();
        sBundle.putString("COMPONENT_TYPE", entrance);
    }

    public static Bundle getRnCommonActivityBundle() {
        return sBundle;
    }
}
