package com.example.cmacking.reactnativeandroiddemo.RnView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;


import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class MyRnViewActivity extends ReactActivity {
    //构建一个阻塞的单一数据的队列
    public static ArrayBlockingQueue<String> mQueue = new ArrayBlockingQueue<String>(1);

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "HelloWorld";
    }

    /**
     * 打开 带返回的Activity
     *
     * 通过 该 Activity_A 打开新的 Activity_B 后，在 Activity_B
     * 执行 this.setResult(Activity.RESULT_OK,new Intent().putExtra(key)); this.finish 关闭 Activity_B 并回传参数
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
            String result = data.getStringExtra("get_result");
            Toast.makeText(this, result, Toast.LENGTH_LONG);
            if (result != null && !result.equals("")) {
                mQueue.add(result);
            } else {
                mQueue.add("无数据啦");
            }
        } else {
            mQueue.add("没有回调...");
        }
    }

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new MyRnViewActivityDelegate(this, getMainComponentName());
    }

    //自定义MyRnViewActivityDelegate
    class MyRnViewActivityDelegate extends ReactActivityDelegate {

        public MyRnViewActivityDelegate(Activity activity, @javax.annotation.Nullable String mainComponentName) {
            super(activity, mainComponentName);
        }

        @javax.annotation.Nullable
        @Override
        protected Bundle getLaunchOptions() {
            Bundle bundle = new Bundle();
            bundle.putString("bundle", "android传递的初始化参数");
            return bundle;
        }
    }

}

//
//public class MyRnViewActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private TextView mTextMessage;
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//    };
//
//    private Integer count = 0;
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.button:
//                count++;
//                Toast.makeText(MainActivity.this, "方式1 count:" + count, Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.button2:
//                Toast.makeText(MainActivity.this, "方式2 count:" + count, Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(MainActivity.this, MyActivity.class);
//                startActivity(intent);
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//        ((Button) findViewById(R.id.button)).setOnClickListener(this);
//        ((Button) findViewById(R.id.button2)).setOnClickListener(this);
//
////        Button button2 = (Button) findViewById(R.id.button2);
////        button2.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Toast.makeText(MainActivity.this, "方式二", Toast.LENGTH_SHORT)
////                        .show();
////            }
////        });
//
////        Button button3 = (Button) findViewById(R.id.button2);
////        button3.setOnClickListener(new CustomButtonListener());
//    }
//
////    public class CustomButtonListener implements View.OnClickListener {
////
////        @Override
////        public void onClick(View v) {
////            Toast.makeText(MainActivity.this, "方式三", Toast.LENGTH_SHORT).show();
////        }
////
////    }
//
//
//    public void onclick_button1(View view) {
//        Toast.makeText(this, "方式一", Toast.LENGTH_SHORT).show();
//    }
//}
