package com.example.lovenews.base;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.lovenews.bean.NewsData;

/**
 * Created by 若兰 on 2016/1/16.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class TabDetailPager extends BaseMenuDetailPager {

    NewsData.NewsTabData mTabData;
    private TextView tvTitle;

    public TabDetailPager(Activity activity,NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
    }

    @Override
    public View initViews() {

        tvTitle = new TextView(mActivity);
        return tvTitle;
    }

    @Override
    public void initData() {
        tvTitle.setText(mTabData.title);
    }
}
