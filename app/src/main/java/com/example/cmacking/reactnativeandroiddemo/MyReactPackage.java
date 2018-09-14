package com.example.cmacking.reactnativeandroiddemo;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 注册模块
 */

public class MyReactPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
//        List<NativeModule> modules = new ArrayList<>();
//        modules.add(new MyIntentModule(reactContext));
//        return modules;

        return Arrays.<NativeModule>asList(new MyIntentModule(reactContext));
    }
    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

}
