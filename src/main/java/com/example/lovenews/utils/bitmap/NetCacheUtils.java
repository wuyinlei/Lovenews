package com.example.lovenews.utils.bitmap;

/**
 * Created by 若兰 on 2016/1/19.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络缓存
 */
public class NetCacheUtils {

    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;


    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        mLocalCacheUtils = localCacheUtils;
        mMemoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 从网络加载图片
     *
     * @param tvImage   图片
     * @param listimage 连接
     */
    public void getBitmapFormNet(ImageView tvImage, String listimage) {

        new BitmapTask().execute(tvImage, listimage);//启动AsyncTask   传参数
        //在doInBackground里面获取到
    }

    /**
     * Handler和线程池的封装
     * <p/>
     * AsyncTask的三个参数、
     * <p/>
     * 第一个参数 Params  参数泛型    doInBackground
     * 第二个参数 Progress  更新进度的泛型   onProgressUpdate
     * 第三个参数 Result   返回结果    onPostExecute
     */
    class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView ivPic;
        private String url;

        /**
         * 后台韩式方法再次执行，子线程
         *
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            ivPic = (ImageView) params[0];
            url = (String) params[1];

            ivPic.setTag(url);

            Bitmap bitmap = downloadBitmap(url);
            Log.d("BitmapTask", "从网络读取文件");
            return bitmap;
        }

        /**
         * 进度回调   UI线程中更新
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         * 耗时方法结束后，执行此方法  UI线程
         *
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                //确保图片设定给了正确的ImageView
                String bindUrl = (String) ivPic.getTag();
                if (url.equals(bindUrl)) {
                    ivPic.setImageBitmap(bitmap);
                    //在获取到网络图片之后，把图片保存到本地
                    mLocalCacheUtils.setBitmapToLocal(url, bitmap);
                    //将图片保存到内存
                    mMemoryCacheUtils.setBitmapToMemory(url,bitmap);
                }
            }
        }
    }

    /**
     * 下载图片
     *
     * @param url
     */
    private Bitmap downloadBitmap(String url) {
        HttpURLConnection conn = null;
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                is = conn.getInputStream();

                bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
