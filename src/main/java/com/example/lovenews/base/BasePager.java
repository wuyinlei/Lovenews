package com.example.lovenews.base;

/**
 * Created by 若兰 on 2016/1/15.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lovenews.R;
import com.example.lovenews.activity.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 主页面下五个页面的基类
 */
public class BasePager {

    public View rootView;
    public Activity mActivity;
    public TextView tvTitle;
    public FrameLayout flContent;
    public ImageView ivMenu,ivPic;

    public BasePager(Activity activity) {
        this.mActivity = activity;
        initViews();
    }

    /**
     * 初始化布局
     */
    public void initViews() {
        rootView = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        flContent = (FrameLayout) rootView.findViewById(R.id.fl_content);
        ivMenu = (ImageView) rootView.findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });
        ivPic = (ImageView) rootView.findViewById(R.id.ivPhoto);
    }

    /**
     * 切换SlidingMenu的状态
     * 表示是不是要显示SlidingMenu
     */
    public void toggleSlidingMenu() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();    //切换状态   显示的时候隐藏，影藏是显示
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 设置侧边栏干开启和关闭
     *
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable){
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        //设置slidingmenu不能拉出来
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
