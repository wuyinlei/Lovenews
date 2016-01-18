package com.example.lovenews.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovenews.R;
import com.example.lovenews.activity.NewsDetailActivity;
import com.example.lovenews.bean.NewsData;
import com.example.lovenews.bean.TabData;
import com.example.lovenews.contants.Contants;
import com.example.lovenews.utils.CacheUtils;
import com.example.lovenews.utils.PrefUtils;
import com.example.lovenews.view.RefreshListView;
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
    private RefreshListView mListView;

    private List<TabData.TopNewsData> mTopnews;
    private List<TabData.TabNewsData> mNewsDataList;
    private String mMoreUrl;
    private NewsAdapter adapter;
    private String mRead_ids;

    private Handler mHandler;

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;

        //初始化地址
        mUrl = Contants.BASE_URL + mTabData.url;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);

        /**
         * 加载头布局
         */
        View headerView = View.inflate(mActivity, R.layout.list_header_top_news, null);
        //注解
        ViewUtils.inject(this, view);
        ViewUtils.inject(this, headerView);

        //将头条新闻以头布局的相识加给listview
        mListView.addHeaderView(headerView);

        /**
         * 设置下拉刷新的事件监听
         */
        mListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {

            /**
             * 下拉刷新
             */
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            /**
             * 上拉加载更多
             */
            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                    mListView.OnRefreshComplete(false);  //收起脚部局
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(mActivity, "position:" + position, Toast.LENGTH_SHORT).show();

                /**
                 * 每个新闻都有一个id，我们记录id，然后就能设置已读和未读
                 *
                 * 3222,22511,5541,44515,45414
                 */

                mRead_ids = PrefUtils.getString(mActivity, "read_ids", "");
                String ids = mNewsDataList.get(position).id;
                /**
                 * 在本地记录是否阅读了新闻
                 *
                 * 如果包含了不加
                 */
                if (!mRead_ids.contains(ids)) {
                    mRead_ids = mRead_ids + ids + ",";
                    PrefUtils.setString(mActivity, "read_ids", mRead_ids);
                }
                //实现局部界面刷新   view---->被点击的item的对象
                changeReadState(view);
                //adapter.notifyDataSetChanged();

                /**
                 * 跳转到新闻详情页面
                 */
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", mNewsDataList.get(position).url);
                mActivity.startActivity(intent);

            }
        });

        return view;
    }

    /**
     * 改变已经读的颜色
     */
    private void changeReadState(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setTextColor(Color.GRAY);
    }

    /**
     * 初始话数据
     */
    @Override
    public void initData() {
        //tvTitle.setText(mTabData.title);

        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache, false);
        }
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
                parseData(result, false);

                mListView.OnRefreshComplete(true);

                /**
                 * 保存缓存
                 */
                CacheUtils.setCache(mUrl, result, mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                        .show();
                error.printStackTrace();
                mListView.OnRefreshComplete(false);
            }
        });
    }

    /**
     * 请求服务器数据
     * <p/>
     * 请求更多数据
     */
    private void getMoreDataFromServer() {

        HttpUtils utils = new HttpUtils();

        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                //System.out.println("返回结果:" + result);
                //Log.d("TabDetailPager", "页签详情页" + result);
                parseData(result, true);

                mListView.OnRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                        .show();
                error.printStackTrace();
                mListView.OnRefreshComplete(false);
            }
        });
    }


    /**
     * 解析数据
     *
     * @param result 数据的结果
     * @param isMore 是否加载更多
     */
    private void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        mFromTabData = gson.fromJson(result, TabData.class);

        /**
         * 获取到下一页数据的地址
         */
        String more = mFromTabData.data.more;
        /**
         * 处理更多页面的逻辑
         */
        if (!TextUtils.isEmpty(more)) {
            //地址拼接
            mMoreUrl = Contants.BASE_URL + more;
        } else {
            //否则设置为空
            mMoreUrl = null;
        }
        //Log.d("TabDetailPager", "mFromTabData:" + mFromTabData);

        if (!isMore) {

           /* mNewsDataList = mFromTabData.data.news;
            //判断是否为空
            if (mNewsDataList != null) {
                adapter = new NewsAdapter();
                mListView.setAdapter(adapter);
            }*/
            initLists();

           /* mTopnews = mFromTabData.data.topnews;
            //判断是否为空
            if (mTopnews != null) {
                mTextView.setText(mTopnews.get(0).title);
                *//**
             * 在拿到数据之后在去设置adapter
             *//*
                mViewPager.setAdapter(new TopNewsAdapter());
                //mViewPager.setOnPageChangeListener(this);
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);//快照显示
                mIndicator.setOnPageChangeListener(this);

                //让指示器重新定位到第一个
                mIndicator.onPageSelected(0);
            }*/
            indicatorInit();

            /**
             * 自动轮播条显示
             */
            if (mHandler == null) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        int currentItem = mViewPager.getCurrentItem();
                        if (currentItem < mTopnews.size() - 1) {
                            currentItem++;
                        } else {
                            currentItem = 0;
                        }

                        //切换到下一个页面
                        mViewPager.setCurrentItem(currentItem);

                        //继续延时3秒发消息
                        mHandler.sendEmptyMessageDelayed(0, 3000);
                    }
                };

                //延时3秒发消息
                mHandler.sendEmptyMessageDelayed(0, 3000);
            }
        } else {
            //如果是加载下一页，需要将数据追加到集合中
            List<TabData.TabNewsData> news = mFromTabData.data.news;
            mNewsDataList.addAll(news);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化listview数据
     */
    private void initLists() {
        mNewsDataList = mFromTabData.data.news;
        //判断是否为空
        if (mNewsDataList != null) {
            adapter = new NewsAdapter();
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

            //view的复用
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.list_news_item, null);
                holder.mIvImage = (ImageView) convertView.findViewById(R.id.ivImage);
                holder.mTvData = (TextView) convertView.findViewById(R.id.tvData);
                holder.mTvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TabData.TabNewsData item = getItem(position);
            holder.mTvData.setText(item.pubdate);
            mBitmapUtils.display(holder.mIvImage, item.listimage);
            holder.mTvTitle.setText(item.title);

            mRead_ids = PrefUtils.getString(mActivity, "read_ids", "");
            if (mRead_ids.contains(getItem(position).id)) {
                holder.mTvTitle.setTextColor(Color.GRAY);
            } else {
                holder.mTvTitle.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }

    static class ViewHolder {
        private TextView mTvTitle;
        private TextView mTvData;
        private ImageView mIvImage;
    }


}
