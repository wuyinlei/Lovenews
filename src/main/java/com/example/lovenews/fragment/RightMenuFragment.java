package com.example.lovenews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.lovenews.R;
import com.example.lovenews.base.BasePager;
import com.example.lovenews.base.GovaffairsPager;
import com.example.lovenews.base.NewsCenterPager;
import com.example.lovenews.base.SettingPager;
import com.example.lovenews.base.SmartServicePager;
import com.example.lovenews.impl.HomePager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 若兰 on 2016/1/15.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class RightMenuFragment extends BaseFrament {

    @ViewInject(R.id.rgGrope)
    private RadioGroup rgGroup;

    @ViewInject(R.id.viewPager)
    private ViewPager mViewPager;

    private List<BasePager> mPagerList;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_right_content,null);
       // rgGroup = (RadioGroup) view.findViewById(R.id.rgGrope);
        ViewUtils.inject(this, view);   //注入view和事件
        return view;
    }

    @Override
    public void initData() {
        //设置默认勾选项
        rgGroup.check(R.id.rbHome);

        /**
         * 初始化5个子页面
         */
        mPagerList = new ArrayList<>();
       /* for (int i = 0; i < 5; i++) {
            BasePager basePager = new BasePager(mActivity);
            mPagerList.add(basePager);
        }*/

        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new SmartServicePager(mActivity));
        mPagerList.add(new GovaffairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));


        ContentAdapter adapter = new ContentAdapter();
        mViewPager.setAdapter(adapter);
    }

    class ContentAdapter extends PagerAdapter{
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
            BasePager pager = mPagerList.get(position);
            container.addView(pager.rootView);
            //在这个地方调用，要不然没有数据变化
            pager.initData();
            return pager.rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
