package com.example.lovenews.utils;

import android.content.Context;

/**
 * Created by 若兰 on 2016/1/18.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class CacheUtils {

    /**
     * @param key  url
     * @param json value
     */
    public static void setCache(String key, String json, Context context) {
        PrefUtils.setString(context, key, json);
    }

    /**
     * 读取缓存
     */
    public static String getCache(String key,Context context) {
       return PrefUtils.getString(context,key,null);
    }
}
