package com.example.lovenews.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.lovenews.activity.MainActivity;
import com.example.lovenews.base.BaseMenuDetailPager;
import com.example.lovenews.base.BasePager;
import com.example.lovenews.base.menudetail.InteractMenuDetailPager;
import com.example.lovenews.base.menudetail.NewsMenuDetailPager;
import com.example.lovenews.base.menudetail.PhotoMenuDetailPager;
import com.example.lovenews.base.menudetail.TopicMenuDetailPager;
import com.example.lovenews.bean.NewsData;
import com.example.lovenews.contants.Contants;
import com.example.lovenews.fragment.LefeMenuFragment;
import com.example.lovenews.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

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

public class NewsCenterPager extends BasePager {

    private List<BaseMenuDetailPager> mDetailPagers;  //4个详情菜单的集合
    private NewsData mNewsData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        /**
         * 这样就可以动态的设置view了
         */
        //tvTitle.setText("新闻中心");

        setSlidingMenuEnable(true);

        /**
         * 读取缓存
         */
        String cache = CacheUtils.getCache(Contants.CATEGORIES_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            //如果存在缓存，直接解析数据，无需访问网络
            parseData(cache);
        }
        //不管有没有缓存，都去获取最新数据，保证数据最新
        getDataFromService();


    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromService() {
        HttpUtils utils = new HttpUtils();

        //String url = "http://192.168.1.100:8080/categories.json";
        // 使用xutils发送请求
        utils.send(HttpMethod.GET, Contants.CATEGORIES_URL,
                new RequestCallBack<String>() {

                    // 访问成功
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        String result = (String) responseInfo.result;
                        //System.out.println("返回结果:" + result);
                        parseData(result);

                        /**
                         * 设置缓存
                         */
                        CacheUtils.setCache(Contants.CATEGORIES_URL, result, mActivity);

                    }

                    // 访问失败
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                                .show();
                        error.printStackTrace();
                    }

                });
    }

    /**
     * 解析json数据
     *
     * @param result
     */
    private void parseData(String result) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsData.class);
        // Log.d("NewsCenterPager", "newsData:" + newsData);

        //刷新侧边栏数据
        MainActivity mainUi = (MainActivity) mActivity;
        LefeMenuFragment lefeMenuFragment = mainUi.getLeftMenuFragmentTag();
        lefeMenuFragment.setMenuData(mNewsData);

        /**
         * 四个菜单详情页
         */
        mDetailPagers = new ArrayList<>();
        mDetailPagers.add(new NewsMenuDetailPager(mActivity, mNewsData.data.get(0).children));
        mDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mDetailPagers.add(new PhotoMenuDetailPager(mActivity,ivPic));
        mDetailPagers.add(new InteractMenuDetailPager(mActivity));

        //设置菜单详情页----新闻为默认当前页
        setCurrentMenuDetailPager(0);
    }

    /**
     * 设置当前菜单详情页
     */
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager pager = mDetailPagers.get(position);
        flContent.removeAllViews();//清除之前依附在framelayout上面的页面
        flContent.addView(pager.mRootView); // 将当前要显示的菜单详情页布局文件设置给framelayout

        //设置当前页的标题
        NewsData.NewsMenuData newsMenuData = mNewsData.data.get(position);
        tvTitle.setText(newsMenuData.title);

        //初始化当前页面的数据
        pager.initData();

        if (pager instanceof PhotoMenuDetailPager) {
            ivPic.setVisibility(View.VISIBLE);
        } else {
            ivPic.setVisibility(View.GONE);
        }
    }
}
