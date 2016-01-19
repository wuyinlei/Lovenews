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
import android.os.Environment;

import com.example.lovenews.utils.MD5Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 本地缓存
 */
public class LocalCacheUtils {

    /**
     * 获取到手机sdcard绝对路径
     */
    public static final String CACHE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/aixuexi";


    /**
     * 从本地sdcard中读取bitmap
     *
     * @param url
     */
    public Bitmap getBitmapFromLocal(String url) {
        try {
            String fileName = MD5Encoder.encode(url);

            //创建一个名字为fileName的文件夹
            File file = new File(CACHE_PATH, fileName);
            //如果这个文件夹存在，就直接通过bitmap工厂来获取到图片，并设置
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 向sdcard设置图片
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            String fileName = MD5Encoder.encode(url);

            //创建文件夹
            File file = new File(CACHE_PATH, fileName);

            //得到这个文件夹的父文件夹
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {// 如果文件夹不存在, 创建文件夹
                parentFile.mkdirs();
            }

            // 将图片保存在本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
