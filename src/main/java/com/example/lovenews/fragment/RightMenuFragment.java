package com.example.lovenews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.lovenews.R;
import com.example.lovenews.base.BasePager;
import com.example.lovenews.base.GovaffairsPager;
import com.example.lovenews.base.HomePager;
import com.example.lovenews.base.NewsCenterPager;
import com.example.lovenews.base.SettingPager;
import com.example.lovenews.base.SmartServicePager;
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
        View view = View.inflate(mActivity, R.layout.fragment_right_content, null);
        // rgGroup = (RadioGroup) view.findViewById(R.id.rgGrope);
        ViewUtils.inject(this, view);   //注入view和事件
        return view;
    }

    @Override
    public void initData() {
        //设置默认勾选项
        rgGroup.check(R.id.rbHome);

        /**
         * 设置RadioGroup的选择事件
         */
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbHome:
                        //设置当前页   去掉页面切换的平滑效果
                        mViewPager.setCurrentItem(0,false);

                        break;
                    //设置当前页
                    case R.id.rbNewscenter:
                        mViewPager.setCurrentItem(1,false);
                        break;
                    //设置当前页
                    case R.id.rbSmartservice:
                        mViewPager.setCurrentItem(2,false);
                        break;
                    //设置当前页
                    case R.id.rbGovaffairs:
                        mViewPager.setCurrentItem(3,false);
                        break;
                    //设置当前页
                    case R.id.rbSetting:
                        mViewPager.setCurrentItem(4,false);
                        break;
                    default:
                        break;
                }
            }
        });


        mPagerList = new ArrayList<>();
       /* for (int i = 0; i < 5; i++) {
            BasePager basePager = new BasePager(mActivity);
            mPagerList.add(basePager);
        }*/
        /**
         * 初始化5个子页面，也就是添加五个pager
         */
        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new SmartServicePager(mActivity));
        mPagerList.add(new GovaffairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));


        ContentAdapter adapter = new ContentAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //获取当前被选中的页面，初始化数据
                mPagerList.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * 手动的去初始化首页，要不然自己禁用的SlidingMenu的滑动效果还是有的
         */
        mPagerList.get(0).initData();
    }

    /**
     * ViewPager的适配器
     */
    class ContentAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 创建视图
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagerList.get(position);
            container.addView(pager.rootView);
            //在这个地方调用，要不然没有数据变化
            //pager.initData();   //不要放在这个地方，要不然，每次的ViewPager会预先加载三个页面，自己和左右
            return pager.rootView;
        }

        /**
         * 销毁视图
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
