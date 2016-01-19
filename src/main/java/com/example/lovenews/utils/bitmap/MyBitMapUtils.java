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
    private Bitmap mBitmap;

    public MyBitMapUtils(){
        mLocalCacheUtils = new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils);

    }

    /**
     * 三级缓存
     *
     * 内存缓存   优先加载  速度快
     *
     * 本地缓存   次优先加载
     *
     * 网络缓存   不优先加载   速度慢
     *
     * @param tvImage
     * @param listimage
     */

    public void display(ImageView tvImage, String listimage) {

        tvImage.setImageResource(R.mipmap.pic_item_list_default);

        //从内存中读取

        mBitmap = mLocalCacheUtils.getBitmapFromLocal(listimage);
        if (mBitmap !=null){
            tvImage.setImageBitmap(mBitmap);
            Log.d("MyBitMapUtils", "从本地读取图片了");
            return;
        }

        //从本地读取

        //从网络中读取
        mNetCacheUtils.getBitmapFormNet(tvImage,listimage);


    }
}
