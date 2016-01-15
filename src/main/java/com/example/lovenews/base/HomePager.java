package com.example.lovenews.base;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 若兰 on 2016/1/15.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        ivMenu.setVisibility(View.GONE);
        /**
         * 这样就可以动态的设置view了
         */
        tvTitle.setText("爱新闻");

        /**
         * 关闭侧边栏干
         */
        setSlidingMenuEnable(false);

        TextView textView = new TextView(mActivity);
        textView.setText("爱新闻");
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        /**
         * 像framlayout中添加到首页   这个时候，我们可以添加布局和自己想定义的
         */
        flContent.addView(textView);

    }


}
