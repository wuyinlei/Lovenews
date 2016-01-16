package com.example.lovenews.base;

/**
 * Created by 若兰 on 2016/1/16.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页
 */
public abstract class BaseMenuDetailPager {

    public Activity mActivity;

    public View mRootView;

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initViews();
    }

    /**
     * 初始化view
     */
    public abstract View initViews();

    /**
     * 初始化数据
     */
    public void initData(){

    }
}
