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
import android.util.LruCache;

/**
 * 内存缓存
 */
public class MemoryCacheUtils {

    /**
     * 软引用
     * <p/>
     * Java中的引用
     * -强引用  垃圾回收器不会回收，java默认应用为强引用
     * -软引用  SoftReference  在内存不够的时候，垃圾回收器会考虑回收
     * -弱引用  WeakReference  在内存不够的时候，垃圾回收器会优先考虑回收
     * -虚引用  PhantomReference 在内存不够的时候，垃圾回收器会最优先考虑回收
     */
    //  private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<>();


    /**
     * 利用LruCache进行缓存，有效的解决了OOM问题
     */
    private LruCache<String, Bitmap> mBitmapLruCache;

    public MemoryCacheUtils() {
        long maxMemory = Runtime.getRuntime().maxMemory() / 8;//模拟器单个app最大内存
        mBitmapLruCache = new LruCache<String, Bitmap>((int) maxMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //int byteCount = value.getByteCount();   API12以上可以使用

                //获取图片占用内存大小
                int byteCount = value.getRowBytes() * value.getHeight();
                return byteCount;
            }
        };//定制最大内存的1/8
    }

    /**
     * 从内存中读取缓存的图片
     *
     * @param url
     */
    public Bitmap getBitmapFromMemory(String url) {

       /* SoftReference<Bitmap> softReference = mMemoryCache.get(url);
        if (softReference != null) {
            Bitmap bitmap = softReference.get();
            return bitmap;
        }*/
        return mBitmapLruCache.get(url);
    }

    /**
     * 把图片设置给内存
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToMemory(String url, Bitmap bitmap) {
       /* SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);

        mMemoryCache.put(url, softReference);*/
        mBitmapLruCache.put(url, bitmap);
    }
}
