package com.example.lovenews.utils.bitmap;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.example.lovenews.R;

/**
 * Created by 若兰 on 2016/1/19.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

/**
 * 自定义图片加载工具
 */
public class MyBitMapUtils {

    NetCacheUtils mNetCacheUtils;
    LocalCacheUtils mLocalCacheUtils;
    MemoryCacheUtils mMemoryCacheUtils;
    private Bitmap mBitmap = null;

    public MyBitMapUtils() {
        mLocalCacheUtils = new LocalCacheUtils();
        mMemoryCacheUtils = new MemoryCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);

    }

    /**
     * 三级缓存
     * <p/>
     * 内存缓存   优先加载  速度快
     * <p/>
     * 本地缓存   次优先加载
     * <p/>
     * 网络缓存   不优先加载   速度慢
     *
     * @param image
     * @param url
     */

    public void display(ImageView image, String url) {

        image.setImageResource(R.mipmap.pic_item_list_default);

        /**
         *   从内存中读取
         */
        mBitmap = mMemoryCacheUtils.getBitmapFromMemory(url);
        if (mBitmap != null) {
            image.setImageBitmap(mBitmap);
            return;
        }

        /**
         * 从本地读取图片
         */
        mBitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if (mBitmap != null) {
            image.setImageBitmap(mBitmap);
            Log.d("MyBitMapUtils", "从本地读取图片了");
            //将图片保存在内存
            mMemoryCacheUtils.setBitmapToMemory(url, mBitmap);
            return;
        }


        /**
         * 从网络中读取图片
         */
        mNetCacheUtils.getBitmapFormNet(image, url);


    }
}
