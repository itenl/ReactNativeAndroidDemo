package com.example.cmacking.reactnativeandroiddemo.RnView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.cmacking.reactnativeandroiddemo.MainApplication;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;

import java.util.concurrent.ArrayBlockingQueue;

public class CommonActivity extends ReactActivity {
    //构建一个阻塞的单一数据的队列
//    public static ArrayBlockingQueue<String> mQueue = new ArrayBlockingQueue<String>(1);

    private static String COMPONENT_TYPE = "";

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "RnCommonActivity";
    }

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        COMPONENT_TYPE = this.getIntent().getStringExtra("COMPONENT_TYPE");
//    }

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegate(this, getMainComponentName()) {
            @Nullable
            @Override
            protected Bundle getLaunchOptions() {
//                Bundle bundle = new Bundle();
//                bundle.putString("COMPONENT_TYPE", COMPONENT_TYPE);
//                return bundle;
                return MainApplication.getRnCommonActivityBundle();
            }
        };
    }
}
