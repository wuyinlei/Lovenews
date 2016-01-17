package com.example.lovenews.base;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovenews.R;
import com.example.lovenews.bean.NewsData;
import com.example.lovenews.bean.TabData;
import com.example.lovenews.contants.Contants;
import com.example.lovenews.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

/**
 * Created by 若兰 on 2016/1/16.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {


    NewsData.NewsTabData mTabData;

    /**
     *
     */
    private String mUrl = null;
    private TabData mFromTabData;

    /**
     * 头条新闻位置指示器
     */
    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;

    @ViewInject(R.id.viewPager)
    private TopNewsViewPager mViewPager;

    /**
     * 头条新闻标题
     */
    @ViewInject(R.id.tvTitle)
    private TextView mTextView;

    @ViewInject(R.id.lv_list)
    private ListView mListView;

    private List<TabData.TopNewsData> mTopnews;
    private List<TabData.TabNewsData> mNewsDataList;

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = Contants.BASE_URL + mTabData.url;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);

        /**
         * 加载头布局
         */
        View headerView = View.inflate(mActivity,R.layout.list_header_top_news,null);
        //注解
        ViewUtils.inject(this, view);
        ViewUtils.inject(this,headerView);

        //将头条新闻以头布局的相识加给listview
        mListView.addHeaderView(headerView);
        return view;
    }

    /**
     * 初始话数据
     */
    @Override
    public void initData() {
        //tvTitle.setText(mTabData.title);
        getDataFromServer();


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTextView.setText(mTopnews.get(position).title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * ViewPager适配器
     */
    class TopNewsAdapter extends PagerAdapter {

        private BitmapUtils mBitmapUtils;

        public TopNewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            //设置默认的加载的图片
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mFromTabData.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView image = new ImageView(mActivity);
            //image.setImageResource(R.mipmap.topnews_item_default);
            //完全匹配父控件的宽高
            image.setScaleType(ImageView.ScaleType.FIT_XY);

            TabData.TopNewsData topNewsData = mFromTabData.data.topnews.get(position);

            //传递imageview对象和图片地址
            mBitmapUtils.display(image, topNewsData.topimage);

            container.addView(image);

            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 请求服务器数据
     */
    private void getDataFromServer() {

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                //System.out.println("返回结果:" + result);
                //Log.d("TabDetailPager", "页签详情页" + result);
                parseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                        .show();
                error.printStackTrace();
            }
        });
    }

    /**
     * 解析数据
     *
     * @param result
     */
    private void parseData(String result) {
        Gson gson = new Gson();
        mFromTabData = gson.fromJson(result, TabData.class);
        //Log.d("TabDetailPager", "mFromTabData:" + mFromTabData);

        indicatorInit();

        initLists();
    }

    /**
     * 初始化listview数据
     */
    private void initLists() {
        mNewsDataList = mFromTabData.data.news;
        //判断是否为空
        if (mNewsDataList != null) {
            NewsAdapter adapter = new NewsAdapter();
            mListView.setAdapter(adapter);
        }
    }

    /**
     * 初始化Indicator和viewpager的数据
     */
    private void indicatorInit() {

        mTopnews = mFromTabData.data.topnews;
        //判断是否为空
        if (mTopnews != null) {
            mTextView.setText(mTopnews.get(0).title);
            /**
             * 在拿到数据之后在去设置adapter
             */
            mViewPager.setAdapter(new TopNewsAdapter());
            //mViewPager.setOnPageChangeListener(this);
            mIndicator.setViewPager(mViewPager);
            mIndicator.setSnap(true);//快照显示
            mIndicator.setOnPageChangeListener(this);

            //让指示器重新定位到第一个
            mIndicator.onPageSelected(0);
        }
    }

    /**
     * 新闻列表的适配器
     */
    class NewsAdapter extends BaseAdapter {

        private BitmapUtils mBitmapUtils;

        public NewsAdapter() {

            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mNewsDataList.size();
        }

        @Override
        public TabData.TabNewsData getItem(int position) {
            return mNewsDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null){
                holder = new ViewHolder();
                convertView = View.inflate(mActivity,R.layout.list_news_item,null);
                holder.mIvImage = (ImageView) convertView.findViewById(R.id.ivImage);
                holder.mTvData = (TextView) convertView.findViewById(R.id.tvData);
                holder.mTvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }
            TabData.TabNewsData item = getItem(position);
            holder.mTvData.setText(item.pubdate);
            mBitmapUtils.display(holder.mIvImage,item.listimage);
            holder.mTvTitle.setText(item.title);

            return convertView;
        }
    }

    static class ViewHolder{
        private TextView mTvTitle;
        private TextView mTvData;
        private ImageView mIvImage;
    }



}
