package com.example.lovenews.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.lovenews.R;

import java.util.ArrayList;

public class GuideActivity extends BaseActivity {

    private ViewPager viewPager;
    private LinearLayout ll_point_group;
    private ArrayList<ImageView> imageViews;
    private View redPoint;

    private static final int[] mImages = new int[]{R.mipmap.guide_1, R.mipmap.guide_2
            , R.mipmap.guide_3};

    //小圆点之间的距离
    private int mPointWidth;
    private LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initilizes();
        initViews();
        initAdapter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    private void initilizes() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        redPoint = findViewById(R.id.redPoint);
    }

    /**
     * 初始化数据
     */
    private void initViews() {

        imageViews = new ArrayList<>();

        //初始化引导页的3个页面
        for (int i = 0; i < mImages.length; i++) {
            ImageView imageView = new ImageView(this);
            //设置引导页背景
            imageView.setBackgroundResource(mImages[i]);
            imageViews.add(imageView);
        }

        /**
         * 初始化引导页的小圆点
         */
        for (int i = 0; i < mImages.length; i++) {
            View point = new View(this);
            params = new LinearLayout.LayoutParams(20, 20);

            if (i > 0) {
                params.leftMargin = 10; //设置圆点间隔
            }

            point.setLayoutParams(params);//设置引导页原点的大小
            point.setBackgroundResource(R.drawable.shape_point_gray);//设置小圆点
            ll_point_group.addView(point);
        }


        /**
         * 获取视图树   对layout结束事件进行监听
         */
        ll_point_group.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //当layout执行完成后执行此方法
            @Override
            public void onGlobalLayout() {
                //移除当前的
                ll_point_group.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //下面的额是在API16以上版本
                //ll_point_group.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                /**
                 * 两个小圆点之间的距离
                 */
                mPointWidth = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();

            }
        });

    }

    private void initAdapter() {
        GuideAdapter adapter = new GuideAdapter();
        viewPager.setAdapter(adapter);

        GuidePagerListener listener = new GuidePagerListener();

        viewPager.setOnPageChangeListener(listener);
    }

    /**
     * ViewPager的适配器
     */
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }

    /**
     * ViewPager的滑动监听
     */
    class GuidePagerListener implements ViewPager.OnPageChangeListener {

        /**
         * 滑动的状态
         *
         * @param position
         * @param positionOffset       偏移
         * @param positionOffsetPixels 偏移多少像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //获取移动的百分比  在这里还要加入position，要不然，每次都是0
            float len = mPointWidth * positionOffset + position * mPointWidth;

            //获取当前红点的布局参数
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) redPoint.getLayoutParams();
            params.leftMargin = (int) len;

            //重新给小红点设置参数
            redPoint.setLayoutParams(params);
        }

        /**
         * 选中的状态
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position) {

        }

        /**
         * 滑动状态改变的时候调用
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
