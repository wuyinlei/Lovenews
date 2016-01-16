package com.example.lovenews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.lovenews.R;
import com.example.lovenews.fragment.LefeMenuFragment;
import com.example.lovenews.fragment.ContentFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    private FragmentManager fm;

    /**
     * ctrl + shift + y----->变成小写
     * <p/>
     * ctrl + shift + x----->变成大写
     */
    private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";

    private static final String FRAGMENT_RIGHT_CONTENT = "fragment_right_content";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //设置侧边栏
        setBehindContentView(R.layout.left_menu);

        //获取SlidingMenu对象
        SlidingMenu slidingMenu = getSlidingMenu();

        //设置全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //设置预留屏幕的宽度
        slidingMenu.setBehindOffset(300);

        initFragment();
    }

    /**
     * 初始化fragment，将fragment数据填充给布局文件
     */
    private void initFragment() {
        fm = getSupportFragmentManager();
        FragmentTransaction fmt = fm.beginTransaction();//开启事务

        //替换id和fragment   替换FrameLayout   并加入标记
        fmt.replace(R.id.left_menu, new LefeMenuFragment(), FRAGMENT_LEFT_MENU);
        fmt.replace(R.id.right_content, new ContentFragment(), FRAGMENT_RIGHT_CONTENT);
        //提交事物
        fmt.commit();
    }

    /**
     * 获取侧边栏对象
     */
    public LefeMenuFragment getLeftMenuFragmentTag() {
        FragmentManager fm = getSupportFragmentManager();
        LefeMenuFragment fragment = (LefeMenuFragment) fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
        return fragment;
    }

    /**
     * 获取主页面对象
     */
    public ContentFragment getRightFragmentTag() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(FRAGMENT_RIGHT_CONTENT);
        return fragment;
    }

}
