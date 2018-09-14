package com.example.cmacking.reactnativeandroiddemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cmacking.reactnativeandroiddemo.RnView.CommonActivity;
import com.example.cmacking.reactnativeandroiddemo.RnView.MyRnViewActivity;
import com.example.cmacking.reactnativeandroiddemo.RnView.TestViewActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.hudl.oss.react.fragment.ReactFragment;

import java.util.HashMap;
import java.util.Stack;

public class WelcomeActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {

    private static final String COMPONENT_FRAGMENT_NAME = "HelloWorld";//HelloWorld TestView GetAllNativeModules

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Activity currentActivity = this;
        String result = currentActivity.getIntent().getStringExtra("params");
        Toast.makeText(this, "Android.WelComeActivity.onCreate.params:" + result, Toast.LENGTH_SHORT).show();
        Button btn_welcome = (Button) this.findViewById(R.id.btn_welcome);
        Button btn_testview = (Button) this.findViewById(R.id.btn_testview);
        Button btn_rncommonactivity = (Button) this.findViewById(R.id.btn_rncommonactivity);
        btn_welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(WelcomeActivity.this, MyRnViewActivity.class);
//                mIntent.putExtra("","");
                WelcomeActivity.this.startActivity(mIntent);
            }
        });
        btn_testview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(WelcomeActivity.this, TestViewActivity.class);
                WelcomeActivity.this.startActivity(mIntent);
            }
        });

        //通过公共页 随机跳转到 Rn 页
        btn_rncommonactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = (int) ((Math.random() * 3 + 1));
                MainApplication.setRnCommonActivityInitProps(num);
                Intent mIntent = new Intent(WelcomeActivity.this, CommonActivity.class);
//                mIntent.putExtra("COMPONENT_TYPE","TestView");
                WelcomeActivity.this.startActivity(mIntent);
            }
        });


//        ActivityManager sd = (ActivityManager) this.getSystemService(this.ACTIVITY_SERVICE);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            // FRAGMENT 传参
            bundle.putString("msg", "{\"test\":123,\"str\":\"HelloWorld this message from Android_WelcomeActivity_Fragment\"}");
            ReactFragment reactFragment = new ReactFragment.Builder(COMPONENT_FRAGMENT_NAME).setLaunchOptions(bundle).build();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.layout_welcome, reactFragment)
                    .commit();
        }


    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    /**
     * Forward onKeyUp events to the ReactFragment in order to handle double tap reloads
     * and dev menus
     *
     * @param keyCode
     * @param event
     * @return true if event was handled
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = false;
        Fragment activeFragment = getSupportFragmentManager().findFragmentById(R.id.layout_welcome);
        if (activeFragment instanceof ReactFragment) {
            handled = ((ReactFragment) activeFragment).onKeyUp(keyCode, event);
        }
        return handled || super.onKeyUp(keyCode, event);
    }
}
