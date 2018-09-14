package com.example.cmacking.reactnativeandroiddemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmacking.reactnativeandroiddemo.util.ThemeUtile;

/**
 * 1、 原生页面与RN 跳转交互
 * 2、 黑白主题切换
 */

public class MyActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtile.changeTheme(this);
        setContentView(createView());
    }

    private View createView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setGravity(Gravity.CENTER);
        ll.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 设置文字
        TextView mTextView = new TextView(this);
        mTextView.setText("hello world 这里是原生页面：MyActivity");
//        TypedValue typedValue = new TypedValue();
//        getTheme().resolveAttribute(R.attr.Text_bg_Color, typedValue, true);
//        mTextView.setBackgroundColor(typedValue.resourceId);

        //设置按钮
        Button mButton = new Button(this);
        mButton.setId(R.id.btn_change_theme);
        mButton.setText(R.string.change_theme);
        mButton.setOnClickListener(this);

        //设置按钮
        Button mButton_jump = new Button(this);
        mButton_jump.setId(R.id.btn_jumpactivity);
        mButton_jump.setText(R.string.jump_activity);
        mButton_jump.setOnClickListener(this);


        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // 在父类布局中添加它，及布局样式
        ll.addView(mTextView, mLayoutParams);
        ll.addView(mButton, mLayoutParams);
        ll.addView(mButton_jump, mLayoutParams);


        return ll;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_theme:
                if (ThemeUtile.night) {
                    ThemeUtile.night = false;
                } else {
                    ThemeUtile.night = true;
                }
                recreate();
                break;
            case R.id.btn_jumpactivity:
                Intent mIntent = new Intent();
                mIntent.putExtra("get_result", "From Activity的数据回调过来啦~");
                MyActivity.this.setResult(Activity.RESULT_OK, mIntent);
                MyActivity.this.finish();
                break;
            default:
                break;
        }

        //出现问题：由于在当前屏幕值重新设置主题，会导致重新调用onCreate()方法导致闪屏

        /**
         * 解决方案1：
         * 可以在一个新开的Activity中通过ThemeUtile.night变量重新设置主题，但不调用recreate()方法切换主题，
         * 而是“手动”设置需要改变的属性，在退出该Activity时，使用Intent回到之前的界面，
         * 并 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
         * ,使之前的Activity全部出栈，重新创建一个新的Activity，执行onCreate()方法，从而改变主题。
         * 虽然不是很优雅，但是完成了不闪屏切换Android App主题。
         */
    }
}


