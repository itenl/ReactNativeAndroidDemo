package com.example.cmacking.reactnativeandroiddemo.RnView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cmacking.reactnativeandroiddemo.R;
import com.facebook.react.ReactActivity;

//public class TestViewActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_view2);
//    }
//}

public class TestViewActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "TestView";
    }
}
