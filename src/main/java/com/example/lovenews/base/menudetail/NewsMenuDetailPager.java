package com.example.lovenews.base.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lovenews.R;
import com.example.lovenews.activity.MainActivity;
import com.example.lovenews.base.BaseMenuDetailPager;
import com.example.lovenews.base.TabDetailPager;
import com.example.lovenews.bean.NewsData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 若兰 on 2016/1/16.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

/**
 * 菜单详情页---新闻
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private TabPageIndicator mIndicator;

    @ViewInject(R.id.iv_next)
    private ImageView iv_next;

    private List<TabDetailPager> mPagerList;

    /**
     * 页签网络数据
     */
    List<NewsData.NewsTabData> mNewsTabData;

    public NewsMenuDetailPager(Activity activity, List<NewsData.NewsTabData> children) {
        super(activity);
        mNewsTabData = children;
    }

    @Override
    public View initViews() {

        View view = View.inflate(mActivity, R.layout.menu_news_center_details, null);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        ViewUtils.inject(this, view);

        /**
         * 初始化TabPageIndicator
         */
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);


        return view;
    }

    /**
     * 跳转到下一个页面
     */
    @OnClick(R.id.iv_next)
    public void nextPage(View view) {
        int currentItem = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++currentItem);
    }

    @Override
    public void initData() {

        mPagerList = new ArrayList<>();

        //初始化页签数据
        for (int i = 0; i < mNewsTabData.size(); i++) {
            TabDetailPager tabDetailPager = new TabDetailPager(mActivity, mNewsTabData.get(i));
            mPagerList.add(tabDetailPager);
        }

        mViewPager.setAdapter(new MenuDetailAdapter());
        /**
         *   当ViewPager和Indicator在一起绑定的时候，如果给ViewPager设置滑动绑定事件
         是不能的，要给Indicator设置滑动绑定事件
         */

        // mViewPager.setOnPageChangeListener(this);
        mIndicator.setOnPageChangeListener(this);

        /**
         * 把viewpager设置给指示器
         *
         * 要在viewpager设置完成adapter之后才能设置
         */

        mIndicator.setViewPager(mViewPager);


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();

        //只有在第一个页面，北京这个页面，侧边栏才能出来
        if (position == 0) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * ViewPager适配器
     */
    class MenuDetailAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            TabDetailPager pager = mPagerList.get(position);
            container.addView(pager.mRootView);

            //初始化数据
            pager.initData();
            return pager.mRootView;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mNewsTabData.get(position).title;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
