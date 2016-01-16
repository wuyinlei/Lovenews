package com.example.lovenews.base.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.lovenews.R;
import com.example.lovenews.base.BaseMenuDetailPager;
import com.example.lovenews.base.TabDetailPager;
import com.example.lovenews.bean.NewsData;
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
public class NewsMenuDetailPager extends BaseMenuDetailPager {

    private ViewPager mViewPager;
    private TabPageIndicator mIndicator;

    private List<TabDetailPager> mPagerList;

    /**
     * 页签网络数据
     */
    List<NewsData.NewsTabData> mNewsTabData;

    public NewsMenuDetailPager(Activity activity,List<NewsData.NewsTabData> children) {
        super(activity);
        mNewsTabData= children;
    }

    @Override
    public View initViews() {

        View view = View.inflate(mActivity, R.layout.menu_news_center_details,null);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        /**
         * 初始化TabPageIndicator
         */
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);


        return view;
    }

    @Override
    public void initData() {

        mPagerList = new ArrayList<>();

        //初始化页签数据
        for (int i = 0; i < mNewsTabData.size(); i++) {
            TabDetailPager tabDetailPager = new TabDetailPager(mActivity,mNewsTabData.get(i));
            mPagerList.add(tabDetailPager);
        }

        mViewPager.setAdapter(new MenuDetailAdapter());

        /**
         * 把viewpager设置给指示器
         *
         * 要在viewpager设置完成adapter之后才能设置
         */
        mIndicator.setViewPager(mViewPager);


    }

    class MenuDetailAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
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
