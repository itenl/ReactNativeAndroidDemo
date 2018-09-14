package com.example.cmacking.reactnativeandroiddemo;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.cmacking.reactnativeandroiddemo.RnView.MyRnViewActivity;
import com.example.cmacking.reactnativeandroiddemo.util.JSONHelper;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.uimanager.PixelUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 原生Activity与React交互——模块
 */

public class MyIntentModule extends ReactContextBaseJavaModule {

    public MyIntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "MyIntentModule";
    }
    //注意：记住getName方法中的命名名称，JS中调用需要

    @ReactMethod
    public void startActivityFromJS(String name, String params) {
        try {
            Activity currentActivity = getCurrentActivity();
            if (null != currentActivity) {
                Log.i("info", params);
                Class toActivity = Class.forName(name);
                Intent intent = new Intent(currentActivity, toActivity);
                Toast.makeText(currentActivity, "Android.MyIntentModule.startActivityFromJS.params 这是由RN传递过来的参数：Key params => " + params, Toast.LENGTH_SHORT).show();
                intent.putExtra("params", params);
                currentActivity.startActivity(intent);
            }
        } catch (Exception e) {
            throw new JSApplicationIllegalArgumentException(
                    "不能打开Activity : " + e.getMessage());
        }
    }

    @ReactMethod
    public void startActivityFromJSGetResult(String className, int requestCode, Callback successBack, Callback errorBack) {
        try {
            Activity currentActivity = getCurrentActivity();
            if (currentActivity != null) {
                Class toActivity = Class.forName(className);
                Intent intent = new Intent(currentActivity, toActivity);
                currentActivity.startActivityForResult(intent, requestCode);
                //进行回调数据
                successBack.invoke(MyRnViewActivity.mQueue.take());
            }
        } catch (Exception e) {
            errorBack.invoke(e.getMessage());
            e.printStackTrace();
        }

    }

    @ReactMethod
    public void dataToJS(Callback successBack, Callback errorBack) {
        try {
            Activity currentActivity = getCurrentActivity();
            String result = currentActivity.getIntent().getStringExtra("params");
            if (TextUtils.isEmpty(result)) {
                result = "没有数据";
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            successBack.invoke(result);
        } catch (Exception e) {
            errorBack.invoke(e.getMessage());
        }
    }

    @ReactMethod
    public void dataToJSByPromise(
            int tag,
            int ancestorTag,
            Promise promise) {
        try {

            WritableMap map = Arguments.createMap();

            map.putDouble("relativeX", tag + 96);
            map.putDouble("relativeY", tag + 97);
            map.putDouble("width", ancestorTag + 98);
            map.putDouble("height", ancestorTag + 99);
            Thread.sleep(2000);
            promise.resolve(map);
        } catch (IllegalViewOperationException e) {
            promise.reject(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static final String DURAION_SHORT_KEY = "SHORT";
    private static final String DURAION_LONG_KEY = "LONG";
    private static final String TEST_OBJECT_KEY = "OBJ";

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURAION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURAION_LONG_KEY, Toast.LENGTH_LONG);
        String str = null;
        try {
            str = GetUesrByStr();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        constants.put(TEST_OBJECT_KEY, str);
        return constants;
    }

    public String GetUesrByStr() throws JSONException {
        User user = new User();
        user.setName("abcd");
        user.setPassword("123456");

        User user1 = new User();
        user1.setName("abcdf");
        user1.setPassword("1234567");

        String jsonStrUser = JSONHelper.toJSON(user);   //序列化
//        User jsonUser = JSONHelper.parseObject(jsonStrUser, User.class);    //反序列化
//        Map mapUser = JSONHelper.parseObject(jsonStrUser, HashMap.class);   //反序列化


        List sourceList = new ArrayList<User>();
        sourceList.add(user);
        sourceList.add(user1);
        String jsonStrUserList = JSONHelper.toJSON(sourceList);         //序列化
//        List listUser = (List) JSONHelper.parseCollection(jsonStrUserList, List.class, User.class); //反序列化

        return jsonStrUserList;
    }

    //    Boolean -> Bool
//    Integer -> Number
//    Double -> Number
//    Float -> Number
//    String -> String
//    Callback -> function
//    ReadableMap -> Object
//    ReadableArray -> Array
    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    //注意：startActivityFromJS、dataToJS方法添加RN注解(@ReactMethod)，否则该方法将不被添加到RN中
}

class User {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

