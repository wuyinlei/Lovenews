package com.example.lovenews.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.lovenews.base.BasePager;

/**
 * Created by 若兰 on 2016/1/15.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }
    @Override
    public void initData() {
        ivMenu.setVisibility(View.GONE);
        /**
         * 这样就可以动态的设置view了
         */
        tvTitle.setText("设置");

        /**
         * 关闭侧边栏干
         */
        setSlidingMenuEnable(false);

        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        /**
         * 像framlayout中添加到首页
         */
        flContent.addView(textView);

    }
}
