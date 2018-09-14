package com.example.cmacking.reactnativeandroiddemo.util;

import android.app.Activity;

import com.example.cmacking.reactnativeandroiddemo.R;

/**
 * 切换主题——工具
 */

public class ThemeUtile {

    public static boolean night = false;


    public static void changeTheme(Activity activity){
        if (ThemeUtile.night){
            activity.setTheme(R.style.APPThemeNight);
        }else{
            activity.setTheme(R.style.AppTheme);
        }
    }
}
