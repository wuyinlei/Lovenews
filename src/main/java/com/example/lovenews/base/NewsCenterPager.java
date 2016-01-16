package com.example.lovenews.base;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovenews.bean.NewsData;
import com.example.lovenews.contants.Contants;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * Created by 若兰 on 2016/1/15.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class NewsCenterPager extends BasePager {
    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        /**
         * 这样就可以动态的设置view了
         */
        tvTitle.setText("新闻中心");

        setSlidingMenuEnable(true);

        getDataFromService();
        TextView textView = new TextView(mActivity);
        textView.setText("新闻中心");
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        /**
         * 像framlayout中添加到首页
         */
        flContent.addView(textView);



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
     * @param result
     */
    private void parseData(String result) {
        Gson gson = new Gson();
        NewsData newsData = gson.fromJson(result, NewsData.class);
        Log.d("NewsCenterPager", "newsData:" + newsData);
    }
}
